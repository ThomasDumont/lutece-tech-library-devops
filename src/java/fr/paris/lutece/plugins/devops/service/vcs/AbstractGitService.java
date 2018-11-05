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

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.plugins.devops.business.CommandResult;
import fr.paris.lutece.plugins.devops.util.DevopsUtils;
import fr.paris.lutece.plugins.devops.util.PathUtils;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.plugins.devops.vcs.git.GitUser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class AbstractGitService implements IVCSService<GitUser>
{
    public static final String MASTER_BRANCH = "master";
    public static final String DEVELOP_BRANCH = "develop";
    private static final String CONSTANTE_REF_TAG = "refs/tags/";

    @Override
    public void init( )
    {
        throw new UnsupportedOperationException( "Not supported yet." ); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String doCheckoutSite( String strSiteName, String strUrl, GitUser user, CommandResult commandResult, String strCommit, boolean bTag )
    {
        String strClonePath = PathUtils.getPathCheckoutSite( strSiteName );
        cloneOrReturnGit( strClonePath, strUrl, commandResult, user, strCommit, bTag );

        return StringUtils.EMPTY;
    }

    @Override
    public boolean isPrivate( )
    {
        return true;
    }

    @Override
    public ReferenceList getTagsRefList( String strUrlSite, GitUser user, String strSiteName, CommandResult commandResult )
    {
        String strClonePath = PathUtils.getPathCheckoutSite( strSiteName );
        Git git = cloneOrReturnGit(strClonePath, strUrlSite, commandResult, user, null, false );
        Collection<String> listTagName = getTagsList( git );

        ReferenceList refList = new ReferenceList( );
        for ( String strTagName : listTagName )
        {
            ReferenceItem item = new ReferenceItem( );
            item.setName( strTagName );
            item.setCode( strTagName );
            refList.add( item );
        }

        return refList;
    }

    public static Collection<String> getTagsList( Git git )
    {
        List<String> listTagName = new ArrayList<>( );
        try
        {
            Collection<Ref> colTags = git.getRepository( ).getRefDatabase( ).getRefsByPrefix( Constants.R_TAGS );
            for ( Ref ref : colTags )
            {
                listTagName.add( ref.getName( ).replace( CONSTANTE_REF_TAG, "" ) );
            }
        }
        catch (IOException e)
        {
            AppLogService.error( e.getMessage( ), e );
        }

        return listTagName;
    }

    public static Git cloneOrReturnGit( String strClonePath, String strRepoURL, CommandResult commandResult, GitUser user, String strCommit, boolean bTag )
    {
        Git git = null;
        Repository repository = null;

        try
        {
            File gitFile = new File( strClonePath + "/.git" );

            if ( gitFile.exists( ) )
            {
                git = Git.open( gitFile );
                checkout( git, strCommit, bTag, commandResult );
                return git;
            }

            clone( strClonePath, strRepoURL, user, commandResult );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        finally
        {
            repository.close( );
        }

        return git;
    }

    public static Git clone( String strClonePath, String strRepoURL, GitUser user, CommandResult commandResult )
    {
        Git git = null;
        Repository repository = null;

        try
        {
            FileRepositoryBuilder builder = new FileRepositoryBuilder( );
            File gitDir = new File( strClonePath );
            repository = builder.setGitDir( gitDir ).readEnvironment( ).findGitDir( ).build( );

            CloneCommand clone = Git.cloneRepository( )
                    .setBare( false )
                    .setCloneAllBranches( true )
                    .setDirectory( gitDir )
                    .setURI( strRepoURL );

            if ( !StringUtils.isEmpty( user.getLogin( ) ) && !StringUtils.isEmpty( user.getPassword( ) ) )
            {
                clone.setCredentialsProvider( new UsernamePasswordCredentialsProvider( user.getLogin( ), user.getPassword( ) ) );
            }
            git = clone.call( );

            createLocalBranch( git, DEVELOP_BRANCH, commandResult );
            createLocalBranch( git, MASTER_BRANCH, commandResult );

            repository.getConfig( ).setString( "user", null, "name", user.getLogin( ) );
            repository.getConfig( ).save( );
        }
        catch( InvalidRemoteException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( TransportException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( GitAPIException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        finally
        {
            repository.close( );
        }

        return git;
    }

    public static void createLocalBranch( Git git, String strBranchName, CommandResult commandResult )
    {
        try
        {
            git.branchCreate( )
                    .setName( strBranchName )
                    .setUpstreamMode( CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM )
                    .setStartPoint( "origin/" + strBranchName )
                    .setForce( true )
                    .call( );
        }
        catch( InvalidRemoteException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( TransportException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( GitAPIException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }

    public static String getRefBranch( Git git, String strBranchName, CommandResult commandResult )
    {
        String refLastCommit = null;
        try
        {
            git.checkout( ).setName( strBranchName ).call( );
            refLastCommit = getLastCommitId( git );
        }
        catch( RefAlreadyExistsException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( RefNotFoundException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( InvalidRefNameException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( CheckoutConflictException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( GitAPIException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }

        return refLastCommit;
    }

    public static PullResult pullRepoBranch( Git git, String strBranchName ) throws IOException, WrongRepositoryStateException,
            InvalidConfigurationException, DetachedHeadException, InvalidRemoteException, CanceledException, RefNotFoundException, NoHeadException,
            TransportException, GitAPIException
    {
        PullResult pPullResult = git.pull( ).setRemoteBranchName( strBranchName ).call( );
        return pPullResult;
    }

    public static void pushForce( Git git, String strRefSpec, GitUser user ) throws InvalidRemoteException, TransportException,
            GitAPIException
    {
        git.push( )
                .setRemote( "origin" )
                .setRefSpecs( new RefSpec( strRefSpec ) )
                .setForce( true )
                .setCredentialsProvider( new UsernamePasswordCredentialsProvider( user.getLogin( ), user.getPassword( ) ) )
                .call( );
    }

    public static MergeResult mergeRepoBranch( Git git, String strBranchToMerge ) throws IOException, WrongRepositoryStateException,
            InvalidConfigurationException, DetachedHeadException, InvalidRemoteException, CanceledException, RefNotFoundException, NoHeadException,
            TransportException, GitAPIException
    {
        List<Ref> call = git.branchList( ).call( );
        Ref mergedBranchRef = null;
        for ( Ref ref : call )
        {
            if ( ref.getName( ).equals( "refs/heads/" + strBranchToMerge ) )
            {
                mergedBranchRef = ref;
                break;
            }
        }
        MergeResult mergeResult = git.merge( ).include( mergedBranchRef ).call( );

        return mergeResult;
    }

    public static MergeResult mergeBack( Git git, String strUserName, String strPassword, CommandResult commandResult ) throws IOException, GitAPIException
    {

        Ref tag = getTagLinkedToLastRelease( git );

        git.checkout( ).setName( MASTER_BRANCH ).call( );
        List<Ref> call = git.branchList( ).call( );

        Ref mergedBranchRef = null;
        for ( Ref ref : call )
        {
            if ( ref.getName( ).equals( "refs/heads/" + DEVELOP_BRANCH ) )
            {
                mergedBranchRef = ref;
                break;
            }
        }

        if ( tag != null )
        {
            mergedBranchRef = tag;
        }
        MergeResult mergeResult = git.merge( ).include( mergedBranchRef ).call( );
        if ( mergeResult.getMergeStatus( ).equals( MergeResult.MergeStatus.CHECKOUT_CONFLICT )
                || mergeResult.getMergeStatus( ).equals( MergeResult.MergeStatus.CONFLICTING )
                || mergeResult.getMergeStatus( ).equals( MergeResult.MergeStatus.FAILED )
                || mergeResult.getMergeStatus( ).equals( MergeResult.MergeStatus.NOT_SUPPORTED ) )
        {
            AppLogService.error( mergeResult.getMergeStatus( ).toString( ) + "\nPlease merge manually master into"
                    + DEVELOP_BRANCH + "branch." );
        }
        else
        {
            git.push( ).setCredentialsProvider( new UsernamePasswordCredentialsProvider( strUserName, strPassword ) ).call( );
            commandResult.getLog( ).append( mergeResult.getMergeStatus( ) );
        }
        return mergeResult;

    }

    private static Ref getTagLinkedToLastRelease( Git git ) throws GitAPIException
    {
        final String TOKEN = "[maven-release-plugin] prepare release ";
        Ref res = null;
        String sTagName = null;

        Iterable<RevCommit> logList = git.log( ).setMaxCount( 10 ).call( );
        Iterator i = logList.iterator( );
        String strCommitMessages = "";
        while ( i.hasNext( ) )
        {
            RevCommit revCommit = (RevCommit) i.next( );
            strCommitMessages = revCommit.getFullMessage( );
            int index = strCommitMessages.indexOf( TOKEN );
            if ( index >= 0 )
            {
                sTagName = strCommitMessages.replace( TOKEN, "" );
                break;
            }
        }

        if ( ( sTagName != null ) && ( !( sTagName.trim( ).equals( "" ) ) ) )
        {
            List<Ref> tags = git.tagList( ).call( );
            for ( int j = 0; j < tags.size( ); j++ )
            {
                Ref tag = tags.get( tags.size( ) - 1 - j );
                String tagName = tag.getName( );
                if ( tagName.equals( "refs/tags/" + sTagName ) )
                {
                    res = tag;
                    break;
                }
            }
        }

        return res;
    }

    public static String getLastLog( Git git, int nMaxCommit ) throws NoHeadException, GitAPIException
    {
        Iterable<RevCommit> logList = git.log( ).setMaxCount( 1 ).call( );
        Iterator i = logList.iterator( );
        String strCommitMessages = "";
        while ( i.hasNext( ) )
        {
            RevCommit revCommit = (RevCommit) i.next( );
            strCommitMessages += revCommit.getFullMessage( );
            strCommitMessages += "\n";
            strCommitMessages += revCommit.getCommitterIdent( );
        }
        return strCommitMessages;
    }

    public static String getLastCommitId( Git git ) throws NoHeadException, GitAPIException
    {
        Iterable<RevCommit> logList = git.log( ).setMaxCount( 1 ).call( );
        Iterator i = logList.iterator( );
        String strCommitId = null;
        while ( i.hasNext( ) )
        {
            RevCommit revCommit = (RevCommit) i.next( );
            strCommitId = revCommit.getName( );
        }
        return strCommitId;
    }

    public static void checkout( Git git, String strCommit, boolean bTag, CommandResult commandResult )
    {
        if (bTag)
        {
            strCommit = CONSTANTE_REF_TAG + strCommit;
        }

        try
        {
            git.checkout( ).setName( strCommit ).setForce( true ).call( );
        }
        catch( InvalidRemoteException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( TransportException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( GitAPIException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }

    @Override
    public boolean checkAuthentication( String strRepoUrl, GitUser user )
    {
        return true;
    }

    public static boolean checkAuthentication( String strRepoUrl, String strLocalTempPath, String strLocalTempDir, GitUser user )
    {
        boolean bAuth = true;
        Path localPath = null;

        try
        {
            Path basePath = Paths.get( strLocalTempPath );
            localPath = Files.createTempDirectory( basePath, strLocalTempDir );
            Git.cloneRepository( ).setURI( strRepoUrl )
                    .setCredentialsProvider( new UsernamePasswordCredentialsProvider( user.getLogin( ), user.getPassword( ) ) )
                    .setDirectory( localPath.toFile( ) )
                    .call( );
        }
        catch( Exception e )
        {
            bAuth = false;
        }
        finally
        {
            if ( localPath != null )
            {
                try
                {
                    FileUtils.forceDelete( new File( localPath.toString( ) ) );
                }
                catch( IOException e )
                {
                    AppLogService.error( "Unable to delete dir " + localPath.toString( ), e );
                }
            }
        }

        return bAuth;
    }
}