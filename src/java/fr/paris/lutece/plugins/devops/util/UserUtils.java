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

import fr.paris.lutece.plugins.devops.vcs.AbstractVCSUser;
import fr.paris.lutece.plugins.devops.vcs.git.GitUser;
import fr.paris.lutece.plugins.devops.vcs.svn.SvnUser;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.service.user.attribute.AdminUserFieldService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import java.util.ArrayList;
import java.util.Map;

public class UserUtils
{
    public static SvnUser getSvnUser( int nIdAdminUser, Locale locale )
    {
        SvnUser svnUser = null;
        boolean bUsedApplicationAccount = AppPropertiesService.getPropertyBoolean( ConstanteUtils.PROPERTY_SVN_USED_DEPLOYMENT_ACCOUNT, false );
        if ( !bUsedApplicationAccount )
        {
            String strIdAttributeLogin = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_LOGIN );
            String strIdAttributePasssword = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_PASSWORD );
            String strLoginValue = null;
            String strPasswordValue = null;

            Map<String, Object> mapAttributeUser = AdminUserFieldService.getAdminUserFields( nIdAdminUser, locale );

            if ( mapAttributeUser.containsKey( strIdAttributeLogin ) && mapAttributeUser.containsKey( strIdAttributePasssword ) )
            {
                strLoginValue = ( (ArrayList<AdminUserField>) mapAttributeUser.get( strIdAttributeLogin ) ).get( 0 ).getValue( );
                strPasswordValue = ( (ArrayList<AdminUserField>) mapAttributeUser.get( strIdAttributePasssword ) ).get( 0 ).getValue( );

                if ( !StringUtils.isEmpty( strLoginValue ) && !( StringUtils.isEmpty( strPasswordValue ) ) )
                {
                    svnUser = new SvnUser( );
                    svnUser.setLogin( strLoginValue );
                    svnUser.setPassword( strPasswordValue );
                }
            }
        }
        else
        {
            String strApplicationLogin = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_SVN_LOGIN_APPLICATION_DEPLOYMENT );
            String strApplicationPasssword = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_SVN_PASSWORD_APPLICATION_DEPLOYMENT );

            svnUser = new SvnUser( );
            svnUser.setLogin( strApplicationLogin );
            svnUser.setPassword( strApplicationPasssword );
        }

        return svnUser;
    }

    /**
     * Get an empty VCS user based on the repo type of the provided application
     * 
     * @param application
     *            the Application
     * @return an empty VCS user based on the repo type of the provided application
     */
    public static AbstractVCSUser getNewVCSUser( String strKey )
    {
        switch( strKey )
        {
            case ConstanteUtils.CONSTANTE_REPO_TYPE_GITHUB:
                return new GitUser( );
            case ConstanteUtils.CONSTANTE_REPO_TYPE_SVN:
                return new SvnUser( );
            case ConstanteUtils.CONSTANTE_REPO_TYPE_GITLAB:
                return new GitUser( );
        }
        return null;
    }

    /**
     * Get the VCS user from the request and the given application
     * 
     * @param request
     *            the HttpServletRequest
     * @param application
     *            the Application
     * @return the VCS user
     */
    public static AbstractVCSUser getVCSUser( HttpServletRequest request, String strRepoType )
    {
        String strLogin = request.getParameter( ConstanteUtils.PARAM_LOGIN );
        String strPassword = request.getParameter( ConstanteUtils.PARAM_PASSWORD );
        AbstractVCSUser user = getNewVCSUser( strRepoType );

        if ( !StringUtils.isEmpty( strLogin ) && !( StringUtils.isEmpty( strPassword ) ) )
        {
            user.setLogin( strLogin );
            user.setPassword( strPassword );
        }

        return user;
    }
}
