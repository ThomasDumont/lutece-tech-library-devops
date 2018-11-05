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

import java.io.File;
import java.util.List;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.commons.collections.CollectionUtils;

public class PathUtils
{
    public static String getPathSite( String strSiteName )
    {
        String strCheckoutBasePath = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_LOCAL_SITE_BASE_PAH );

        return strCheckoutBasePath + File.separator + strSiteName;
    }

    public static String getPathPomFile( String strPathSite )
    {
        return strPathSite + File.separator + ConstanteUtils.CONSTANTE_POM_XML;
    }

    public static String getPathUpgradeFiles( String strPathSite )
    {        
        String strPath = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_UPGRADE_DIRECTORY_PATH );

        return strPathSite + strPath;
    }

    public static String getPathUpgradeFile( String strPathSite, String strFileName )
    {
        return getPathUpgradeFiles( strPathSite ) + File.separator + strFileName;
    }

    public static String getPathArchiveGenerated( String strPathSite, String strWarName, String strExtension )
    {
        List<String> listFileInTarget = FileUtil.list( strPathSite + File.separator + ConstanteUtils.CONSTANTE_TARGET, strExtension, false );
        if ( !CollectionUtils.isEmpty( listFileInTarget ) && listFileInTarget.size( ) == 1 )
        {
            return strPathSite + File.separator + ConstanteUtils.CONSTANTE_TARGET + File.separator + listFileInTarget.get( 0 );
        }

        return strPathSite + File.separator + ConstanteUtils.CONSTANTE_TARGET + File.separator + strWarName + strExtension;
    }


    public static String getPlateformUrlApplication( String strCodeApplication )
    {
        // String strPlateformEnvironmentBaseUrl = AppPropertiesService.getProperty( PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL );
        return strCodeApplication;
    }

    public static String getPlateformUrlServerApplicationInstances( String strCodeApplication, String strCodeEnvironment, String strServerApplicationType )
    {
        String strPathEnvironment = ( strCodeEnvironment.replace( ConstanteUtils.CONSTANTE_SEPARATOR_POINT, ConstanteUtils.CONSTANTE_SEPARATOR_SLASH ) )
                .toUpperCase( );

        return getPlateformUrlApplication( strCodeApplication ) + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + strPathEnvironment
                + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + getPathServerByType( strServerApplicationType, strCodeEnvironment );
    }

    public static String getPlateformUrlDatabases( String strCodeApplication, String strCodeEnvironment, String strServerApplicationType, String strServerApplicationCode )
    {
        return getPlateformUrlServerApplicationInstances( strCodeApplication, strCodeEnvironment, strServerApplicationType )
                + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
                + strServerApplicationCode.toUpperCase( )
                + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
                + ConstanteUtils.CONSTANTE_ACTION_EXECUTE;
    }

    public static String getPlatformUrlServerApplicationActions( String strCodeApplication, String strCodeEnvironment, String strServerApplicationType, String strServerApplicationCode )
    {
        return getPlateformUrlServerApplicationInstances( strCodeApplication, strCodeEnvironment, strServerApplicationType )
                + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + strServerApplicationCode.toUpperCase( );
    }

    public static String getPlatformUrlServerApplicationAction( String strCodeApplication, String strCodeEnvironment, String strServerApplicationType, String strServerApplicationCode, 
            String strCodeAction )
    {
        return getPlatformUrlServerApplicationActions( strCodeApplication, strCodeEnvironment, strServerApplicationType, strServerApplicationCode ) + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
                + strCodeAction;
    }

    public static String getDeployDirectoryTarget( String strCodeApplication, String strCodeEnvironment, String strServerApplicationType, String strServerApplicationCode, 
            String strFtpDirectoryTarget )
    {
        return getPlateformUrlServerApplicationInstances( strCodeApplication, strCodeEnvironment, strServerApplicationType )
                + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
                + strServerApplicationCode
                + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
                + strFtpDirectoryTarget;
    }

    public static String getContextDirectoryTarget( String strCodeApplication, String strCodeEnvironment, String strServerApplicationType, String strServerApplicationCode )
    {
        return getPlateformUrlServerApplicationInstances( strCodeApplication, strCodeEnvironment, strServerApplicationType )
                + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
                + strServerApplicationCode
                + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
                + ConstanteUtils.CONTEXT_DIRECTORY_NAME;
    }

    public static String getDumpFileDirectory( String strCodeApplication, String strCodeEnvironment, String strServerApplicationType, String strServerApplicationCode, 
            String strFtpDirectoryDump )
    {
        return getPlateformUrlServerApplicationInstances( strCodeApplication, strCodeEnvironment, strServerApplicationType )
                + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
                + strServerApplicationCode
                + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
                + strFtpDirectoryDump + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH;
    }

    private static String getPathServerByType( String strApplicationType, String strEvironementCode )
    {
        String strPathServer = strApplicationType;

        if ( strEvironementCode.contains( "v1" ) )
        {
            if ( ConstanteUtils.CONSTANTE_SERVER_TOMCAT.equals( strApplicationType ) )
            {
                strPathServer = ConstanteUtils.CONSTANTE_SERVER_TOM;
            }
            else
                if ( ConstanteUtils.CONSTANTE_SERVER_MYSQL.equals( strApplicationType ) )
                {
                    strPathServer = ConstanteUtils.CONSTANTE_SERVER_MYS;
                }
        }

        return strPathServer;
    }
}
