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
package fr.paris.lutece.plugins.devops.util;

import fr.paris.lutece.plugins.devops.business.CommandResult;
import fr.paris.lutece.plugins.devops.service.vcs.IVCSService;
import java.util.Date;
import java.util.Locale;


import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;

import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

import java.util.List;

public class DevopsUtils
{
    public static String getLastReleaseVersionDataKey( String strArtifactId )
    {
        return ConstanteUtils.CONSTANTE_LAST_RELEASE_VERSION_PREFIX + strArtifactId ;
    }

    public static String getLastReleaseNextSnapshotVersionDataKey( String strArtifactId )
    {
        return ConstanteUtils.CONSTANTE_LAST_RELEASE_NEXT_SNPASHOT_VERSION_PREFIX + strArtifactId ;
    }

    public static void addInfoError( CommandResult commandResult, String strError, Exception e )
    {
        if ( e != null )
        {
            AppLogService.error( strError, e );
        }
        else
        {
            AppLogService.error( strError );
        }

        if ( commandResult != null )
        {
            commandResult.setError( strError );
            commandResult.setStatus( CommandResult.STATUS_ERROR );
            commandResult.setErrorType( CommandResult.ERROR_TYPE_INFO );
        }
    }

    public static void addTechnicalError( CommandResult commandResult, String strError, Exception e ) throws AppException
    {
        if ( e != null )
        {
            AppLogService.error( strError, e );
        }
        else
        {
            AppLogService.error( strError );
        }

        if ( commandResult != null )
        {
            commandResult.setError( strError );
            commandResult.setStatus( CommandResult.STATUS_ERROR );
            commandResult.setRunning( false );
            commandResult.setErrorType( CommandResult.ERROR_TYPE_STOP );
            commandResult.setDateEnd( new Date( ) );
        }

        if ( e != null )
        {
            throw new AppException( strError, e );
        }
        else
        {
            throw new AppException( strError );
        }
    }

    public static void addTechnicalError( CommandResult commandResult, String strError ) throws AppException
    {
        addTechnicalError( commandResult, strError, null );
    }

    /**
     * This method calls Rest WS archive
     *
     * @param strUrl
     *            the url
     * @param params
     *            the params to pass in the post
     * @param listElements
     *            the list of elements to include in the signature
     * @return the response as a string
     * @throws HttpAccessException
     *             the exception if there is a problem
     */
    public static String callPlateformEnvironmentWs( String strUrl ) throws HttpAccessException
    {
        String strResponse = StringUtils.EMPTY;

        try
        {
            HttpAccess httpAccess = new HttpAccess( );
            strResponse = httpAccess.doGet( strUrl );
        }
        catch( HttpAccessException e )
        {
            String strError = "ArchiveWebServices - Error connecting to '" + strUrl + "' : ";
            AppLogService.error( strError + e.getMessage( ), e );
            throw new HttpAccessException( strError, e );
        }

        return strResponse;
    }

    public static ReferenceList addEmptyRefenceItem( ReferenceList referenceList )
    {
        ReferenceList referenceList2 = new ReferenceList( );

        ReferenceItem referenceItem = new ReferenceItem( );

        referenceItem.setCode( ConstanteUtils.CONSTANTE_EMPTY_STRING );
        referenceItem.setName( ConstanteUtils.CONSTANTE_EMPTY_STRING );

        referenceList2.add( 0, referenceItem );
        referenceList2.addAll( referenceList );

        return referenceList2;
    }

    public static ReferenceList getReferenceListServerType( Locale locale )
    {
        ReferenceList referenceList = new ReferenceList( );
        referenceList.addItem( ConstanteUtils.CONSTANTE_EMPTY_STRING, ConstanteUtils.CONSTANTE_EMPTY_STRING );
        referenceList.addItem( ConstanteUtils.CONSTANTE_SERVER_TOMCAT,
                I18nService.getLocalizedString( ConstanteUtils.PROPERTY_SERVER_TYPE_TOMCAT_LABEL, locale ) );
        referenceList
                .addItem( ConstanteUtils.CONSTANTE_SERVER_MYSQL, I18nService.getLocalizedString( ConstanteUtils.PROPERTY_SERVER_TYPE_MYSQL_LABEL, locale ) );
        referenceList
                .addItem( ConstanteUtils.CONSTANTE_SERVER_HTTPD, I18nService.getLocalizedString( ConstanteUtils.PROPERTY_SERVER_TYPE_HTTPD_LABEL, locale ) );

        return referenceList;
    }

    public static ReferenceList getSimpleReferenceList( List<String> list )
    {
        ReferenceList reflist = new ReferenceList( );
        for ( String strCode : list )
        {
            reflist.addItem( strCode, strCode );

        }
        return reflist;
    }

    /**
     * Get the VCS service from the repo type
     * 
     * @param strKey
     *            the repo type key
     * @return the VCS service
     */
    public static IVCSService getVCSService( String strKey )
    {
        switch( strKey )
        {
            case ConstanteUtils.CONSTANTE_REPO_TYPE_GITHUB:
                return SpringContextService.getBean( ConstanteUtils.BEAN_GITHUB_SERVICE );
            case ConstanteUtils.CONSTANTE_REPO_TYPE_SVN:
                return SpringContextService.getBean( ConstanteUtils.BEAN_SVN_SERVICE );
            case ConstanteUtils.CONSTANTE_REPO_TYPE_GITLAB:
                return SpringContextService.getBean( ConstanteUtils.BEAN_GITLAB_SERVICE );
        }
        return null;
    }
}
