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

import fr.paris.lutece.plugins.devops.business.CommandResult;
import fr.paris.lutece.plugins.devops.vcs.AbstractVCSUser;
import fr.paris.lutece.util.ReferenceList;

public interface IVCSService<T extends AbstractVCSUser>
{
    void init( );

    /**
     * Get the ReferenceList of the tags of a site
     *
     * @param strUrlSite
     *            the Url of the repo
     * @param user
     *            the VCS user
     * @param strSiteName
     *            the name of the site
     * @return the ReferenceList of the tags of the repo
     */
    ReferenceList getTagsRefList( String strUrlSite, T user, String strSiteName, CommandResult commandResult );

    /**
     * Perform the checkout of the site
     *
     * @param strSiteName
     *            the site name
     * @param strUrl
     *            the url of the repo
     * @param user
     *            the VCS user
     * @param commandResult
     *            the command result
     * @param strBranch
     *            the branch
     * @param strTag
     *            the tag
     * @return
     */
    String doCheckoutSite( String strSiteName, String strUrl, T user, CommandResult commandResult, String strCommit, boolean bTag );

    /**
     * Check if the repo is public or private
     *
     * @return true if the repo is private, false otherwise
     */
    boolean isPrivate( );

    /**
     * Check if the provided VCS User is authorized to access to repo
     *
     * @param strRepoUrl
     * @param user
     */
    boolean checkAuthentication( String strRepoUrl, T user );
}
