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
import java.io.IOException;

import org.junit.Test;

import fr.paris.lutece.plugins.devops.util.ConstanteUtils;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.test.LuteceTestCase;
import java.util.Date;

/**
 * SiteServiceTest
 */
public class JiraServiceTest extends LuteceTestCase
{
    @Test
    public void testJiraService() throws IOException
    {
        String strJiraKey=AppPropertiesService.getProperty( "devops.componentTest.jiraKey" );
        String strJiraCurrentVersion=AppPropertiesService.getProperty( "devops.componentTest.currentVersion" );
        String strJiraReleaseVersionName=AppPropertiesService.getProperty( "devops.componentTest.releaseVersion" );
        String strJiraNewVersion=AppPropertiesService.getProperty( "devops.componentTest.releaseNewDeveloppmentVersion");

        CommandResult commandResult = new CommandResult( );
        commandResult.setDateBegin( new Date( ) );
        commandResult.setLog( new StringBuffer( ) );
        commandResult.setRunning( true );
        commandResult.setStatus( CommandResult.STATUS_OK );
        commandResult.setProgressValue( 0 );

        JiraService.getService( ).updateComponentVersions( strJiraKey, strJiraCurrentVersion, strJiraReleaseVersionName, 
                strJiraNewVersion, commandResult );
    }
}