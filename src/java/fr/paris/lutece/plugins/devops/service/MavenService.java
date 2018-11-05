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
package fr.paris.lutece.plugins.devops.service;

import fr.paris.lutece.plugins.devops.business.CommandResult;
import fr.paris.lutece.plugins.devops.business.MavenGoals;
import fr.paris.lutece.plugins.devops.util.ConstanteUtils;
import fr.paris.lutece.plugins.devops.util.DevopsUtils;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.List;

import javax.xml.bind.JAXBException;
import org.apache.commons.lang.StringUtils;

/**
 *
 * MavenService : provides maven command launcher
 *
 */
public class MavenService implements IMavenService
{
    // private static IMavenService _singleton;
    private Invoker _invoker;
    private static IMavenService _singleton;

    private static final String TOKEN_RELEASE_VERSION = "$1";
    private static final String TOKEN_TAG = "$2";
    private static final String TOKEN_DEVELOPMENT_VERSION = "$3";
    private static final String TOKEN_USERNAME = "$4";
    private static final String TOKEN_PASSWORD = "$5";
    private static final String RELEASE_PREPARE_BASE = "release:prepare";
    private static final String RELEASE_PREPARE_ARGS = "-DignoreSnapshots=true" + " -DreleaseVersion=" +
        TOKEN_RELEASE_VERSION + " -Dtag=" + TOKEN_TAG + " -DdevelopmentVersion=" + TOKEN_DEVELOPMENT_VERSION +
        " -DforkMode=never" + " -Dusername=" + TOKEN_USERNAME + " -Dpassword=" + TOKEN_PASSWORD +
        " -Darguments=\"-Dmaven.test.skip=true\" --batch-mode";


    private MavenService( )
    {
    }

    public static IMavenService getService( )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( "devops.MavenService" );
            _singleton.init( );
        }
        return _singleton;
    }

    public void init( )
    {
        _invoker = new DefaultInvoker( );
        _invoker.setMavenHome( new File( AppPropertiesService.getProperty( ConstanteUtils.CONSTANTE_MAVEN_HOME_PATH ) ) );
        _invoker.setLocalRepositoryDirectory( new File( AppPropertiesService.getProperty( ConstanteUtils.CONSTANTE_MAVEN_LOCAL_REPOSITORY ) ) );
    }

    public void saveMvnProfilName( String strProfilValue, String strIdApplication, String strCodeEnvironment, String strCodeServerApplicationInstance )
    {
        DatastoreService.setDataValue( strIdApplication + "_" + strCodeEnvironment + "_" + strCodeServerApplicationInstance, strProfilValue );
    }

    public String getMvnProfilSaved( String strIdApplication, String strCodeEnvironment, String strCodeServerApplicationInstance )
    {
        return DatastoreService.getDataValue( strIdApplication + "_" + strCodeEnvironment + "_" + strCodeServerApplicationInstance, null );
    }

    private synchronized InvocationResult mvnExecute( String strSitePath, List<String> goals, CommandResult commandResult )
    {
        InvocationRequest request = new DefaultInvocationRequest( );
        request.setPomFile( new File( strSitePath + File.separator + ConstanteUtils.CONSTANTE_POM_XML ) );
        request.setGoals( goals );
        request.setShowErrors( true );
        request.setShellEnvironmentInherited( true );

        String strProxyHost = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_PROXY_HOST );
        String strProxyPort = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_PROXY_PORT );

        if(!StringUtils.isEmpty( strProxyHost ) && !StringUtils.isEmpty( strProxyPort ))
        {
            request.setMavenOpts( "-Dhttps.proxyHost=" + strProxyHost + "  -Dhttps.proxyPort=" + strProxyPort + " -Dhttp.proxyHost=" + strProxyHost + "  -Dhttp.proxyPort=" + strProxyPort + " -Dfile.encoding=UTF-8");
        }
        InvocationResult invocationResult = null;
        try
        {
            final StringBuffer sbLog = commandResult.getLog( );

            // logger
            _invoker.setOutputHandler( new InvocationOutputHandler( )
            {
                public void consumeLine( String strLine )
                {
                    sbLog.append( strLine + "\n" );
                }
            } );

            invocationResult = _invoker.execute( request );

            return invocationResult;

        }
        catch( Exception e )
        {

            DevopsUtils.addTechnicalError( commandResult, commandResult.getLog( ).toString( ), e );
        }

        return invocationResult;
    }

    public String mvnSiteAssembly( String strPathPom, String strMavenProfile, CommandResult commandResult )
    {
        List<String> listGoals = MavenGoals.LUTECE_SITE_ASSEMBLY.asList( );
        listGoals.add( "-P " + strMavenProfile );
        listGoals.add( "-U" );
        InvocationResult invocationResult = mvnExecute( strPathPom, listGoals, commandResult );

        int nStatus = invocationResult.getExitCode( );

        if ( nStatus != 0 )
        {
            DevopsUtils.addTechnicalError( commandResult, "Error during Release Prepare exit code is: " + nStatus );
        }

        return "";
    }

    public String mvnReleasePerform( String strPathPom, String strUsername, String strPassword, CommandResult commandResult )
    {
        InvocationResult invocationResult = mvnExecute( strPathPom, MavenGoals.RELEASE_PERFORM.asList( ), commandResult );
        int nStatus = invocationResult.getExitCode( );

        if ( nStatus != 0 )
        {
            DevopsUtils.addTechnicalError( commandResult, "Error during Release Perform exit code is: " + nStatus );
        }

        return "";
    }

    /**
     *
     * mvnReleasePrepare
     *
     * @param strBasePath
     *            chemin sur le disque pour l'acces au composant
     * @param strPluginName
     *            le nom du composant
     * @param strReleaseVersion
     *            la version a release
     * @param strTag
     *            le nom du tag
     * @param strDevelopmentVersion
     *            la prochaine version de developpement (avec -SNAPSHOT)
     * @return le thread
     */
    public String mvnReleasePrepare( String strPathPom, String strReleaseVersion, String strTag, String strDevelopmentVersion, String strUsername,
            String strPassword, CommandResult commandResult )
    {
        List<String> listGoals = createReleasePrepare( strReleaseVersion, strTag, strDevelopmentVersion, strUsername, strPassword );

        InvocationResult invocationResult = mvnExecute( strPathPom, listGoals, commandResult );

        int nStatus = invocationResult.getExitCode( );

        if ( nStatus != 0 )
        {
            DevopsUtils.addTechnicalError( commandResult, "Error during Release Prepare exit code is: " + nStatus );
        }

        return "";
    }

    /**
     * Builds maven arguments for release:prepare (with release:prepare)
     * @param strReleaseVersion release version
     * @param strTag tag
     * @param strDevelopmentVersion development version
     * @return the list
     */
    private static List<String> createReleasePrepare( String strReleaseVersion, String strTag, String strDevelopmentVersion, String strUsername, String strPassword )
    {
        String strArguments = RELEASE_PREPARE_ARGS.replace( TOKEN_DEVELOPMENT_VERSION, strDevelopmentVersion );
        strArguments = strArguments.replace( TOKEN_RELEASE_VERSION, strReleaseVersion );
        strArguments = strArguments.replace( TOKEN_TAG, strTag );
        strArguments = strArguments.replace( TOKEN_USERNAME, strUsername );
        strArguments = strArguments.replace( TOKEN_PASSWORD, strPassword );
        
        List<String> listPrepare = new ArrayList<String>( 2 );
        listPrepare.add( RELEASE_PREPARE_BASE );
        listPrepare.add( strArguments );
        
        return listPrepare;     
    }
}
