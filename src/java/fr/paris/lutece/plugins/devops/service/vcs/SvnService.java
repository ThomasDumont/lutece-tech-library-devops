/*
 * Copyright (c) 2002-2018, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.devops.service.vcs;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNCommitPacket;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNRevisionRange;
import org.tmatesoft.svn.core.ISVNDirEntryHandler;
import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

import fr.paris.lutece.plugins.devops.business.CommandResult;
import fr.paris.lutece.plugins.devops.util.ConstanteUtils;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.plugins.devops.util.DevopsUtils;
import fr.paris.lutece.plugins.devops.util.FileUtil;
import fr.paris.lutece.plugins.devops.util.PathUtils;
import fr.paris.lutece.plugins.devops.vcs.svn.SvnUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

public class SvnService implements IVCSService<SvnUser>
{

    private static IVCSService _instance;
    private static final Comparator<SVNDirEntry> _compareSvnEntries = new Comparator<SVNDirEntry>( )
    {
        @Override
        public int compare( SVNDirEntry o1, SVNDirEntry o2 )
        {
            return -1 * o1.getDate( ).compareTo( o2.getDate( ) );
        }
    };


    private SvnService(  )
    {
    }

    public static IVCSService getService()
    {
        if(_instance == null )
        {
            _instance = SpringContextService.getBean(ConstanteUtils.BEAN_SVN_SERVICE );
            _instance.init( );
        }

        return _instance;
    }

    public void init(  )
    {
        // For using over http:// and https:/
        DAVRepositoryFactory.setup(  );

        // For using over svn:// and svn+xxx:/
        SVNRepositoryFactoryImpl.setup(  );

        // For using over file://
        FSRepositoryFactory.setup(  );
    }

    public static ReferenceList getSvnDirChildren( String strUrlSite, SVNClientManager clientManager ) throws SVNException
    {
        final ReferenceList listSites = new ReferenceList( );
        final SVNURL url;

        final List<SVNDirEntry> listSvnEntries = new ArrayList<SVNDirEntry>( );
        url = SVNURL.parseURIEncoded( strUrlSite );

        SVNRepository repository = SVNRepositoryFactory.create( url, null );

        clientManager.getLogClient( ).doList( repository.getLocation( ), SVNRevision.HEAD, SVNRevision.HEAD, false, false, new ISVNDirEntryHandler( )
        {
            public void handleDirEntry( SVNDirEntry entry ) throws SVNException
            {
                ReferenceItem referenceItem;

                if ( !url.equals( entry.getURL( ) ) )
                {
                    if ( entry.getKind( ) == SVNNodeKind.DIR )
                    {
                        listSvnEntries.add( entry );
                    }
                }
            }
        } );

        Collections.sort( listSvnEntries, _compareSvnEntries );

        for ( SVNDirEntry svnEntry : listSvnEntries )
        {
            listSites.addItem( svnEntry.getName( ), svnEntry.getName( ) );
        }

        return listSites;
    }

    /**
     * Commit
     *
     * @param strSiteName
     *            le nom du site
     * @param strTagName
     *            le nom du tag
     * @param copyClient
     *            le client svn permettant la copie
     * @throws SVNException
     */
    public static void commit( String strPathFile, String strCommitMessage, SVNCommitClient commitClient ) throws SVNException
    {
        SVNCommitPacket commitPacket = commitClient.doCollectCommitItems( new File [ ] {
            new File( strPathFile )
        }, false, false, true );

        if ( !SVNCommitPacket.EMPTY.equals( commitPacket ) )
        {
             commitClient.doCommit( commitPacket, false, strCommitMessage );
        }
    }

    /**
     * Tag un site
     *
     * @param strSiteName
     *            le nom du site
     * @param strTagName
     *            le nom du tag
     * @param copyClient
     *            le client svn permettant la copie
     * @throws SVNException
     */
    public static String tagSite( String strSiteName, String strTagName, String strSrcURL, String strDstURL, SVNCopyClient copyClient ) throws SVNException
    {
        // COPY from trunk to tags/tagName
        SVNURL srcURL = SVNURL.parseURIEncoded( strSrcURL );
        SVNURL dstURL = SVNURL.parseURIEncoded( strDstURL );
        SVNCopySource svnCopySource = new SVNCopySource( SVNRevision.HEAD, SVNRevision.HEAD, srcURL );
        SVNCopySource [ ] tabSVNCopy = new SVNCopySource [ 1];
        tabSVNCopy [0] = svnCopySource;

        SVNCommitInfo info = copyClient.doCopy( tabSVNCopy, dstURL, false, false, false, "[site-release] Tag site " + strSiteName + " to " + strTagName, null );

        if ( info.getErrorMessage( ) != null )
        {
            return info.getErrorMessage( ).getMessage( );
        }

        return null;
    }

    @Override
    public String doCheckoutSite(String strSiteName, String strUrl, SvnUser user, CommandResult commandResult, String strCommit, boolean bTag )
    {
        String strClonePath = PathUtils.getPathCheckoutSite( strSiteName );

        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( user.getLogin( ),
                user.getPassword( ) );

        SVNUpdateClient updateClient = new SVNUpdateClient( authManager, SVNWCUtil.createDefaultOptions( false ) );
        
        try
        {
            checkout( strUrl, strClonePath, updateClient, strCommit, bTag, commandResult );
        }
        catch (SVNException e)
        {
            DevopsUtils.addTechnicalError( commandResult, "Une erreur est survenue lors de la tentative d'authentification avec le svn" + e.getMessage( ), e );
        }

        return StringUtils.EMPTY;
    }

    public static Long checkout( String strUrl, String strCheckoutBaseSitePath, SVNUpdateClient updateClient, String strCommit, boolean bTag, CommandResult result )
            throws SVNException
    {
        Long nLastCommitId = null;
        SVNURL url = SVNURL.parseURIEncoded( strUrl );
        File file = new File( strCheckoutBaseSitePath );

        if ( file.exists( ) )
        {
            if ( !FileUtil.delete( file, result.getLog( ) ) )
            {
                result.setError( result.getLog( ).toString( ) );
                DevopsUtils.addTechnicalError( result, "Fail to delete file" );
            }
        }

        SVNRepository repository = SVNRepositoryFactory.create( url, null );
        final StringBuffer logBuffer = result.getLog( );

        try
        {
            updateClient.setEventHandler( new ISVNEventHandler( )
            {
                public void checkCancelled( ) throws SVNCancelException
                {
                }

                public void handleEvent( SVNEvent event, double progress ) throws SVNException
                {
                    logBuffer.append( ( ( event.getAction( ) == SVNEventAction.UPDATE_ADD ) ? "ADDED " : event.getAction( ) ) + " " + event.getFile( ) + "\n" );
                }
            } );

            SVNRevision commit = SVNRevision.parse( strCommit );

            nLastCommitId = updateClient.doCheckout( repository.getLocation( ), file, SVNRevision.HEAD, commit, true );
        }
        catch( SVNAuthenticationException e )
        {
            DevopsUtils.addTechnicalError( result, "Une erreur est survenue lors de la tentative d'authentification avec le svn" + e, e );

            StringWriter sw = new StringWriter( );
            PrintWriter pw = new PrintWriter( sw );
            e.printStackTrace( pw );

            String errorLog = sw.toString( );
            pw.flush( );
            pw.close( );

            try
            {
                sw.flush( );
                sw.close( );
            }
            catch( IOException ex )
            {
                AppLogService.error( ex.getMessage( ), ex );
            }
        }
        catch( Exception e )
        {
            StringWriter sw = new StringWriter( );
            PrintWriter pw = new PrintWriter( sw );
            e.printStackTrace( pw );

            String errorLog = sw.toString( );
            pw.flush( );
            pw.close( );

            try
            {
                sw.flush( );
                sw.close( );
            }
            catch( IOException ex )
            {
                AppLogService.error( ex.getMessage( ), ex );
            }

            DevopsUtils.addTechnicalError( result, "Une erreur svn est survenue:" + e, e );
        }

        return nLastCommitId;
    }

    public static ReferenceList getSvnSites( String strUrlSite, SVNClientManager clientManager ) throws SVNException
    {
        final ReferenceList listSites = new ReferenceList( );
        final SVNURL url;

        url = SVNURL.parseURIEncoded( strUrlSite );

        SVNRepository repository = SVNRepositoryFactory.create( url, null );

        clientManager.getLogClient( ).doList( repository.getLocation( ), SVNRevision.HEAD, SVNRevision.HEAD, false, false, new ISVNDirEntryHandler( )
        {
            public void handleDirEntry( SVNDirEntry entry ) throws SVNException
            {
                if ( !url.equals( entry.getURL( ) ) )
                {
                    if ( entry.getKind( ) == SVNNodeKind.DIR )
                    {
                        listSites.addItem( entry.getName( ), entry.getName( ) );
                    }
                }
            }
        } );

        return listSites;
    }

    public static Long getLastRevision( String strRepoPath, SvnUser user )
    {
        Long lRevision = null;
        SVNClientManager clientManager = SVNClientManager.newInstance( new DefaultSVNOptions( ), user.getLogin( ), user.getPassword( ) );
        SVNRevision revision;
        try
        {
            File fStrRepo = new File(strRepoPath );
            revision = clientManager.getStatusClient( ).doStatus( fStrRepo, true ).getCommittedRevision( );
            if(revision != null)
            {
                return revision.getNumber( );
            }
        }
        catch( SVNException e )
        {
           AppLogService.error( e );
        }

    return lRevision;
    }

    public static void update( String strRepoPath, SvnUser user )
    {
        SVNClientManager clientManager = SVNClientManager.newInstance( new DefaultSVNOptions( ), user.getLogin( ), user.getPassword( ) );

        try
        {
            File fStrRepo = new File(strRepoPath );
            clientManager.getUpdateClient( ).doUpdate( fStrRepo , SVNRevision.HEAD, true );
        }
        catch( SVNException e )
        {
           AppLogService.error( e );
        }
    }

    public static void revert( String strRepoPath, String strCmUrl, SvnUser user, Long revCurrentCommit, Long lRevertCommit)
    {
        SVNClientManager clientManager = SVNClientManager.newInstance( new DefaultSVNOptions( ), user.getLogin( ), user.getPassword( ) );

        SVNDiffClient diffClient = clientManager.getDiffClient();
        SVNRevision sRevertCommit = SVNRevision.create( lRevertCommit );
        SVNRevision sLastCommit = SVNRevision.create( revCurrentCommit );

        if(revCurrentCommit>lRevertCommit)
        {
            SVNRevisionRange rangeToMerge = new SVNRevisionRange( sLastCommit, sRevertCommit );

            try
            {
                diffClient.doMerge(SVNURL.parseURIEncoded( strCmUrl), sLastCommit, Collections.singleton(rangeToMerge),
                           new File(strRepoPath), SVNDepth.INFINITY, true, false, false, false);
            }
            catch( SVNException e )
            {
               AppLogService.error( e );
            }
        }
    }

    public static String getSvnUrlTagSite( String strScmUrl, String strTagName )
    {
        String strUrl = strScmUrl.contains( ConstanteUtils.CONSTANTE_TRUNK ) ? strScmUrl
                .replace( ConstanteUtils.CONSTANTE_TRUNK, ConstanteUtils.CONSTANTE_TAGS ) : strScmUrl;
        return strUrl + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + strTagName;
    }

    public static String getRepoUrl( String strRepoUrl )
    {
        if ( strRepoUrl != null && strRepoUrl.startsWith( "scm:svn:" ) )
        {
            strRepoUrl = strRepoUrl.substring( 8 );
        }

        return strRepoUrl;
    }

    @Override
    public boolean checkAuthentication( String strRepoUrl, SvnUser user )
    {
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( user.getLogin( ),
                user.getPassword( ) );

        try
        {
            SVNURL url = SVNURL.parseURIEncoded( strRepoUrl );
            SVNRepository repository = SVNRepositoryFactory.create( url, null );
            repository.setAuthenticationManager( authManager );
            repository.testConnection( );
        }
        catch( SVNException e )
        {
            return false;
        }

        return true;
    }

    @Override
    public ReferenceList getTagsRefList(String strUrlSite, SvnUser user, String strSiteName, CommandResult commandResult) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isPrivate( )
    {
        return true;
    }
}
