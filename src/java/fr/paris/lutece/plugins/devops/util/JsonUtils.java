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

import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class JsonUtils
{
    public static List<String> getJSONDictionary( String dictionaryName, String strJSONFlux )
    {
        List<String> jsonCollection = new ArrayList<String>( );
        JSONObject jo = (JSONObject) JSONSerializer.toJSON( strJSONFlux );
        JSONArray jsonArray = jo.getJSONArray( dictionaryName );
        Iterator iterator = jsonArray.iterator( );

        while ( iterator.hasNext( ) )
        {
            jsonCollection.add( (String) iterator.next( ) );
        }

        return jsonCollection;
    }

    public static List<String> getJSONDictionary( String objectName, String dictionaryName, String strJSONFlux )
    {
        List<String> jsonCollection = new ArrayList<String>( );
        JSONObject jo = (JSONObject) JSONSerializer.toJSON( strJSONFlux );

        JSONArray jsonArray = jo.getJSONObject( objectName ).getJSONArray( dictionaryName );
        Iterator iterator = jsonArray.iterator( );

        while ( iterator.hasNext( ) )
        {
            jsonCollection.add( (String) iterator.next( ) );
        }

        return jsonCollection;
    }

    public static JSONObject getJSONOBject( String strJSONFlux )
    {
        JSONObject jo = (JSONObject) JSONSerializer.toJSON( strJSONFlux );

        return jo;
    }

    public static JSONObject getJSONForCommandResult( CommandResult result )
    {
        JSONObject jo = new JSONObject( );
        JSONObject joResult = new JSONObject( );

        try
        {
            jo.put( ConstanteUtils.JSON_STATUS, result.getStatus( ) );

            // pour les logs tr�s longs, on ne prend que la fin
            StringBuffer sbLog = result.getLog( );
            int nMaxLogSize = AppPropertiesService.getPropertyInt( ConstanteUtils.PROPERTY_MAX_LOG_SIZE, ConstanteUtils.CONSTANTE_ID_NULL );
            String strLog;

            if ( nMaxLogSize == -ConstanteUtils.CONSTANTE_ID_NULL )
            {
                nMaxLogSize = ConstanteUtils.CONSTANTE_DEFAULT_LOG_SIZE;
            }

            // sbLog null entre le lancement du thread et la premiere requete
            if ( sbLog != null )
            {
                if ( sbLog.length( ) > nMaxLogSize )
                {
                    strLog = sbLog.substring( sbLog.length( ) - nMaxLogSize );
                }
                else
                {
                    strLog = sbLog.toString( );
                }
            }
            else
            {
                strLog = ConstanteUtils.CONSTANTE_EMPTY_STRING;
            }

            jo.put( ConstanteUtils.JSON_LOG, strLog );
            jo.put( ConstanteUtils.JSON_RUNNING, result.isRunning( ) );
            jo.put( ConstanteUtils.JSON_ERROR, result.getError( ) );
            jo.put( ConstanteUtils.JSON_ERROR_TYPE, result.getErrorType( ) );

            for ( Map.Entry<String, String> resultInformations : result.getResultInformations( ).entrySet( ) )
            {

                joResult.put( resultInformations.getKey( ), resultInformations.getValue( ) );
            }
            jo.put( ConstanteUtils.JSON_RESULT, joResult );

        }
        catch( JSONException e )
        {
            AppLogService.error( "JSON error : " + e.getMessage( ), e );
        }

        return jo;
    }
}
