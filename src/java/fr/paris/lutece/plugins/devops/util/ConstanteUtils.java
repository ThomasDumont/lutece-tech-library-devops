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

public class ConstanteUtils
{
    // Beans
    public static final String BEAN_MAVEN_SERVICE = "devops.mavenService";
    public static final String BEAN_JIRA_SERVICE = "devops.jiraComponentService";
    public static final String BEAN_SVN_SERVICE = "devops.SvnService";
    public static final String BEAN_GITHUB_SERVICE = "devops.GithubService";
    public static final String BEAN_GITLAB_SERVICE = "devops.GitlabService";
    public static final String BEAN_GIT_MAVEN_PREPARE_UPDATE_REMOTE_REPOSITORY = "devops.gitMavenPrepareUpdateRemoteRepository";
    public static final String BEAN_SVN_MAVEN_PREPARE_UPDATE_REMOTE_REPOSITORY = "devops.svnMavenPrepareUpdateRemoteRepository";

    // Constantes
    public static final int CONSTANTE_ID_NULL = -1;
    public static final String CONSTANTE_ALL = "all";
    public static final int CONSTANTE_DEFAULT_LOG_SIZE = 500;
    public static final String REGEX_ID = "^[\\d]+$";
    public static final String CONSTANTE_TAGS = "tags";
    public static final String CONSTANTE_TRUNK = "trunk";
    public static final String CONSTANTE_CHECKOUT_ERROR = "Checkout error";
    public static final String CONSTANTE_SEPARATOR_SLASH = "/";
    public static final String CONSTANTE_SEPARATOR_POINT = ".";
    public static final String CONSTANTE_SEPARATOR_VIRGULE = ",";
    public static final String CONSTANTE_EMPTY_STRING = "";
    public static final String CONSTANTE_STAR = "*";
    public static final String CONSTANTE_MARK_STACKTRACE = "stack trace      ";
    public static final String CONSTANTE_SPACE = " ";
    public static final String CONSTANTE_POM_XML = "pom.xml";
    public static final String CONSTANTE_TARGET = "target";
    public static final String CONSTANTE_MAVEN_HOME_PATH = "devops.mavenHomePath";
    public static final String CONSTANTE_MAVEN_LOCAL_REPOSITORY = "devops.mavenLocalRepository";
    public static final String CONSTANTE__ENVIRONMENT = "devops.environment.";
    public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE = "devops.serverApplicationInstance.";
    public static final String CONSTANTE__ENVIRONMENT_CODE = ".code";
    public static final String CONSTANTE__ENVIRONMENT_NAME = ".name";
    public static final String CONSTANTE__ENVIRONMENT_SERVER_APPLICATION_INSTANCE_LIST = ".serverApplicationInstanceList";
    public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_CODE = ".code";
    public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_NAME = ".name";
    public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_SERVER_NAME = ".serverName";
    public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_FTP_WEBAPP_Url = ".ftpWebAppUrl";
    public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_MAVEN_PROFILE = ".mavenProfile";
    public static final String CONSTANTE_SQL_WHERE = " WHERE ";
    public static final String CONSTANTE_SQL_AND = " AND ";
    public static final String CONSTANTE_SERVER_TOMCAT = "TOMCAT";
    public static final String CONSTANTE_SERVER_TOM = "TOM";
    public static final String CONSTANTE_SERVER_MYSQL = "MYSQL";
    public static final String CONSTANTE_SERVER_MYS = "MYS";
    public static final String CONSTANTE_SERVER_HTTPD = "HTTPD";
    public static final String CONSTANTE_SERVER_PSQ = "PSQ";
    public static final String CONSTANTE_REPO_TYPE_SVN = "svn";
    public static final String CONSTANTE_REPO_TYPE_GITHUB = "github";
    public static final String CONSTANTE_REPO_TYPE_GITLAB = "gitlab";
    public static final String CONSTANTE_BRANCH_DEVELOP = "develop";
    public static final String CONSTANTE_BRANCH_MASTER = "master";
    public static final String CONSTANTE_SERVER_RO = "RO";
    public static final String CONSTANTE_MAX_DEPLOY_SITE_CONTEXT_KEY = "max_deploy_site_context_key";
    public static final String CONSTANTE_ACTION_EXECUTE = "@EXECUTE";
    public static final String CONSTANTE_MAX_RELEASE_CONTEXT_KEY = "max_release_context_key";
    public static final String CONSTANTE_RELEASE_CONTEXT_PREFIX = "release_context_";
    public static final String CONSTANTE_LAST_RELEASE_VERSION_PREFIX = "last_release_version_";
    public static final String CONSTANTE_LAST_RELEASE_NEXT_SNPASHOT_VERSION_PREFIX = "last_release_next_snapshot_version_";
    public static final String CONSTANTE_COMPONENT_PROJECT_PREFIX = "component_project_prefix_";
    public static final String CONSTANTE_SUFFIX_GI = "scm:git";
    public static final String CONSTANTE_SNAPSHOT_VERSION = "-SNAPSHOT";

    public static final String CONSTANTE_TYPE_LUTECE_SIT = "lutece-site";
    public static final String CONSTANTE_GITHUB_ORG_LUTECE_SECTEUR_PUBLI = "lutece-secteur-public";
    public static final String CONSTANTE_GITHUB_ORG_LUTECE_PLATFOR = "lutece-platform";

    public static final String JSON_STATUS = "status";
    public static final String JSON_ERROR_TYPE = "error_type";
    public static final String JSON_LOG = "log";
    public static final String JSON_RUNNING = "running";
    public static final String JSON_ERROR = "error";
    public static final String JSON_STATE = "state";
    public static final String JSON_RESULT = "result";
    public static final String JSON_ACTION_LIST = "action_list";
    public static final String JSON_ACTION_ID = "id";
    public static final String JSON_ACTION_CODE = "action_code";
    public static final String JSON_ACTION_ICON_CSS_CLASS = "icon_css_class";
    public static final String JSON_ACTION_NAME = "name";
    public static final String JSON_ACTION_STATUS = "status";
    public static final String JSON_ACTION_DISPLAY = "display";
    public static final String JSON_ACTION_DESCRIPTION = "description";

    public static final String JSON_JSP_FOM_DISPLAY = "jsp_form_display";
    public static final String JSON_FORM_ERROR = "form_error";
    public static final String JSON_SERVER_STATUS = "server_status";
    public static final String JSON_ID_APPLIACTION = "id_application";
    public static final String JSON_CODE_ENVIRONMENT = "code_environment";
    public static final String JSON_CODE_SERVER_APPLICATION_INSTANCE = "code_server_application_instance";
    public static final String JSON_SERVER_APPLICATION_TYPE = "server_application_type";
    public static final String CONTEXT_DIRECTORY_NAME = "CONTEXT";

    // PROPERTY
    public static final String PROPERTY_PROXY_HOST = "httpAccess.proxyHost";
    public static final String PROPERTY_PROXY_PORT = "httpAccess.proxyPort";

    public static final String PROPERTY_MAX_LOG_SIZE = "devops.maxLogSize";
    public static final String PROPERTY_ENVIRONMENTS_LIST = "devops.environments.list";
    public static final String PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL = "devops.plateformEnvironment.baseUrl";
    public static final String PROPERTY_WEBSERVICE_AREAS_JSON_OBJECT_NAME = "devops.webservice.areas.jsonObjectName";
    public static final String PROPERTY_WEBSERVICE_AREAS_JSON_DICTIONARY_NAME = "devops.webservice.areas.jsonDictionaryName";
    public static final String PROPERTY_WEBSERVICE_ENVIRONMENTS_JSON_OBJECT_NAME = "devops.webservice.environments.jsonObjectName";
    public static final String PROPERTY_WEBSERVICE_EVIRONMENTS_JSON_DICTIONARY_NAME = "devops.webservice.environments.jsonDictionaryName";
    public static final String PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_OBJECT_NAME = "devops.webservice.serverApplicationInstances.jsonObjectName";
    public static final String PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_DICTIONARY_NAME = "devops.webservice.serverApplicationInstances.jsonDictionaryName";
    public static final String PROPERTY_WEBSERVICE_SERVER_ACTIONS_JSON_DICTIONARY_NAME = "devops.webservice.serverActions.jsonDictionaryName";
    public static final String PROPERTY_WEBSERVICE_ACTION_RESULT_JSON_PROPERTY_RESULT = "devops.webservices.serverAction.jsonPropertyResult";
    public static final String PROPERTY_WEBSERVICE_INSTALLABLE_WAR_ACTION_OBJECT_NAME = "devops.webservices.installableWarAction.jsonObjectName";
    public static final String PROPERTY_WEBSERVICE_INSTALLABLE_WAR_ACTION_DICTIONARY_NAME = "devops.webservice.installableWarAction.jsonDictionaryName";
    public static final String PROPERTY_WEBSERVICE_DATABASES_JSON_OBJECT_NAME = "devops.webservice.databases.jsonObjectName";
    public static final String PROPERTY_WEBSERVICE_DATABASES_JSON_DICTIONARY_NAME = "devops.webservice.databases.jsonDictionaryName";
    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_HOST = "devops.serverApplicationFtp.host";
    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_PORT = "devops.serverApplicationFtp.port";
    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_LOGIN = "devops.serverApplicationFtp.userLogin";
    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_PASSWORD = "devops.serverApplicationFtp.userPassword";
    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_DPLOY_DIRECTORY_TARGET = "devops.serverApplicationFtp.deployDirectoryTarget";
    public static final String PROPERTY_CHECKOUT_BASE_PAH = "devops.server.checkout.basePath";
    public static final String PROPERTY_MESSAGE_CHECKOUT_ERROR = "message_checkout_error";
    public static final String PROPERTY_MESSAGE_CHECKOUT_ERROR_SITE_EMPTY = "message_checkout_error_site_empty";
    public static final String PROPERTY_MESSAGE_CHECKOUT_ERROR_LOGIN_MDP_EMPTY = "message_checkout_error_login_mdp_empty";
    public static final String PROPERTY_MESSAGE_REPO_UNAUTHORIZED_ACCESS = "message_unauthorized_repo_access";
    public static final String PROPERTY_TASKS_FORM_WORKFLOW_PAGE_TITLE = "devops.tasks_form_workflow.page_title";
    public static final String PROPERTY_FORM_ACTION_SERVER_PAGE_TITLE = "devops.form_action_server.page_title";

    public static final String PROPERTY_SERVER_TYPE_TOMCAT_LABEL = "devops.server_type_tomcat_label";
    public static final String PROPERTY_SERVER_TYPE_HTTPD_LABEL = "devops.server_type_httpd_label";
    public static final String PROPERTY_SERVER_TYPE_MYSQL_LABEL = "devops.server_type_mysql_label";
    public static final String PROPERTY_UPGRADE_DIRECTORY_PATH = "devops.server.upgradeDirectoryPath";;

    public static final String PROPERTY_LOCAL_SITE_BASE_PAH = "devops.localSiteBasePath";
    public static final String PROPERTY_LOCAL_COMPONENT_BASE_PAH = "devops.localComponentBasePath";

    public static final String PROPERTY_APPLICATION_ACCOUNT_ENABLE = "devops.applicationAccountEnable";
    public static final String PROPERTY_SVN_SITES_URL = "devops.svnSitesUrl";
    public static final String PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_LOGIN = "devops.adminUser.idAttribute.svnLogin";
    public static final String PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_PASSWORD = "devops.adminUser.idAttribute.svnPassword";
    public static final String PROPERTY_SVN_RELEASE_COMPONET_ACCOUNT_LOGIN = "devops.svnReleaseComponetAccount.login";
    public static final String PROPERTY_SVN_RELEASE_COMPONET_ACCOUNT_PASSWORD = "devops.svnReleaseComponetAccount.password";
    public static final String PROPERTY_SVN_USED_DEPLOYMENT_ACCOUNT = "devops.svn.usedDeploymentAccount";
    public static final String PROPERTY_SVN_LOGIN_APPLICATION_DEPLOYMENT = "devops.svn.svnLoginDeployment";
    public static final String PROPERTY_SVN_PASSWORD_APPLICATION_DEPLOYMENT = "devops.svn.svnPasswordDeployment";
    public static final String PROPERTY_SITE_REPOSITORY_LOGIN = "devops.site.repository.login";
    public static final String PROPERTY_SITE_REPOSITORY_PASSWORD = "devops.site.repository.password";
    public static final String PROPERTY_GITHUB_RELEASE_COMPONET_ACCOUNT_LOGIN = "devops.githubReleaseComponetAccount.login";
    public static final String PROPERTY_GITHUB_RELEASE_COMPONET_ACCOUNT_PASSWORD = "devops.githubReleaseComponetAccount.password";
    public static final String PROPERTY_GITHUB_SEARCH_REPO_API = "devops.githubSearchRepoApi";
    public static final String PROPERTY_GITHUB_BASE_URL = "devops.githubBaseUrl";
    public static final String PROPERTY_GITHUB_AUTHORIZED_REPO_NAME = "devops.github.authorizedRepositoryName";
    public static final String PROPERTY_GITLAB_BASE_URL = "devops.gitlabSitesUrl";

    public static final String PROPERTY_MAVEN_LOCAL_REPOSITORY = "devops.mavenLocalRepository";
    public static final String PROPERTY_MAVEN_HOME_PATH = "devops.mavenHomePath";
    public static final String PROPERTY_THREAD_RELEASE_POOL_MAX_SIZE = "devops.threadReleasePoolMaxSize";
    public static final String PROPERTY_NB_SEARCH_ITEM_PER_PAGE_LOAD = "devops.nbSearchItemPerPageLoad";

    public static final String PROPERTY_URL_JIRA_SERVICE = "devops.urlJiraService";

    // STATE
    public static final String CONSTANTE_COMMAND_RESULT_STATUS_EXCEPTION_VALUE = "exception";

    // STATUS SERVER APLLICATION STATE
    public static final Integer STATUS_KO = 0;
    public static final Integer STATUS_OK = 1;

    // ARCHIVE TYPE
    public static final String ARCHIVE_WAR_EXTENSION = ".war";
    public static final String ARCHIVE_ZIP_EXTENSION = ".zip";

    // Regex
    public static final String REGEX_GIT_EXTRACT_ARTIFACT_FROM_URL = "devops.regex.git.artifactId.extractFromUrl";

    // Attributes
    public static final String ATTRIBUTE_VCS_USER = "vcs_user";
    public static final String ATTRIBUTE_RELEASER_USER = "releaser_user";

    //PARAM
    public static final String PARAM_LOGIN = "login";
    public static final String PARAM_PASSWORD = "password";
}
