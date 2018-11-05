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
package fr.paris.lutece.plugins.devops.business;

import java.util.List;

import com.sun.jna.platform.win32.OaIdl.CURRENCY._CURRENCY;

import fr.paris.lutece.plugins.devops.util.ConstanteUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;

/**
 * This is the business class for the object Project
 */
public class Project
{
    // Variables declarations
    private String _strName;
    private String _strArtifactId;
    private String _strComponentType;
    private String _strDescription;
    private String _strBaseSiteUrl;


    /**
     * Returns the Name
     *
     * @return The Name
     */
    public String getName( )
    {
        return _strName;
    }

    /**
     * Sets the Name
     *
     * @param strName
     *            The Name
     */
    public void setName( String _strName )
    {
        this._strName = _strName;
    }

    /**
     * Returns the ArtifactId
     *
     * @return The ArtifactId
     */
    public String getArtifactId( )
    {
        return _strArtifactId;
    }

    /**
     * Sets the ArtifactId
     *
     * @param strArtifactId
     *            The ArtifactId
     */
    public void setArtifactId( String strArtifactId )
    {
        _strArtifactId = strArtifactId;
    }

    /**
     * Returns the ComponentType
     *
     * @return The ComponentType
     */
    public String getComponentType( )
    {
        return _strComponentType;
    }

    /**
     * Sets the ComponentType
     *
     * @param strComponentType
     *            The ComponentType
     */
    public void setComponentType( String strComponentType )
    {
        _strComponentType = strComponentType;
    }

    /**
     * Returns the Description
     *
     * @return The Description
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     *
     * @param strDescription
     *            The Description
     */
    public void setDescription( String _strDescription )
    {
        this._strDescription = _strDescription;
    }

    /**
     * Returns the Base Site URL
     *
     * @return The Base Site URL
     */
    public String getBaseSiteUrl( )
    {
        return _strBaseSiteUrl;
    }

    /**
     * Sets the Base Site URL
     *
     * @param strBaseSiteUrl
     *            The Base Site URL
     */
    public void setBaseSiteUrl( String _strBaseSiteUrl )
    {
        this._strBaseSiteUrl = _strBaseSiteUrl;
    }
}