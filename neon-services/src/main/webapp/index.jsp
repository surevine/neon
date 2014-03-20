<%@ page import="com.surevine.neon.util.SpringApplicationContext" %>
<%@ page import="com.surevine.neon.service.SystemHealthService" %>
<%@ page import="com.surevine.neon.service.bean.SystemHealthServiceBean" %>
<%@ page import="com.surevine.neon.service.bean.ImporterConfigurationServiceBean" %>
<%@ page import="java.util.Map" %>
<%
    SystemHealthService healthService = (SystemHealthService) SpringApplicationContext.getBean("systemHealthService");
    SystemHealthServiceBean systemHealthServiceBean = healthService.getSystemStatus();
%>
<!doctype html>
<html>
<head>
    <link rel="stylesheet" href="css/neon.css" />
    <title>NEON Services Status</title>
</head>
<body>
    <div id="content">
        <h1 class="title">NEON Services Status</h1>
        <h2 class="title">Configuration and system health</h2>
        <div class="segment">
            <div class="segment-header">System Properties</div>
            <div class="segment-content">
                <table class="data">
                    <thead>
                    <tr>
                        <td width="400px">Property</td>
                        <td width="400px">Value</td>
                    </tr>
                    </thead>
                    <%
                        for (Map.Entry<String,String> propEntry:systemHealthServiceBean.getSystemProperties().entrySet()) {
                    %>
                    <tr>
                        <td><%= propEntry.getKey() %></td>
                        <td><%= propEntry.getValue() %></td>
                    </tr>
                    <%
                        }
                    %>
                    </thead>
                </table>
            </div>
        </div>
        
        <div class="segment">
            <div class="segment-header">Importer Configuration</div>
            <div class="segment-content">
                <%
                    for (ImporterConfigurationServiceBean importerConfiguration:systemHealthServiceBean.getImporters()) {
                %>
                <table class="data">
                    <thead>
                        <tr>
                            <td colspan="2"><%= importerConfiguration.getImporterName() %></td>
                        </tr>
                        <tr>
                            <td width="400px">Property</td>
                            <td width="400px">Value</td>
                        </tr>
                    </thead>
                    <%
                        for (Map.Entry<String,String> configEntry: importerConfiguration.getConfiguration().entrySet()) {
                    %>
                    <tr>
                        <td><%= configEntry.getKey() %></td>
                        <td><%= configEntry.getValue() %></td>
                    </tr>
                    <%
                        }
                    %>
                    </thead>
                </table>
                <%
                    }
                %>
            </div>
        </div>
        
        <div class="segment">
            <div class="segment-header">Rest services</div>
            <div class="segment-content">
                <table class="data">
                    <thead>
                    <tr>
                        <td>Service URL</td>
                        <td>HTTP Method</td>
                        <td>Description</td>
                        <td>Request payload</td>
                        <td>Response payload</td>
                    </tr>
                    <tr>
                        <td colspan="5">Profile Service</td>
                    </tr>
                    </thead>
                    <tr>
                        <td>rest/profile/{userID}</td>
                        <td>GET</td>
                        <td>Gets the profile for a user</td>
                        <td>{userID}</td>
                        <td>JSON ProfileBean</td>
                    </tr>
                    <tr>
                        <td>rest/profile</td>
                        <td>POST</td>
                        <td>Adds a userID to the profile service and forces a data import</td>
                        <td>{userID}</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>rest/profile</td>
                        <td>DELETE</td>
                        <td>Removes a userID from the profile service</td>
                        <td>{userID}</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>rest/profile/users</td>
                        <td>GET</td>
                        <td>Gets a summary of all users in the system</td>
                        <td>&nbsp;</td>
                        <td>JSON UserServiceSummaryBean</td>
                    </tr>
                    <thead>
                    <tr>
                        <td colspan="5">Importer Service</td>
                    </tr>
                    </thead>
                
                    <tr>
                        <td>rest/importer</td>
                        <td>GET</td>
                        <td>Forces a data import for the given userID</td>
                        <td>{userID}</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>rest/importer/importerconfig</td>
                        <td>GET</td>
                        <td>Displays an importer's configuration as JSON</td>
                        <td>&nbsp;</td>
                        <td>JSON array of ImporterConfigurationServiceBean</td>
                    </tr>
                    <tr>
                        <td>rest/importer/importerconfig/{importerName}</td>
                        <td>GET</td>
                        <td>Displays an importer's configuration as JSON</td>
                        <td>&nbsp;</td>
                        <td>JSON ImporterConfigurationServiceBean</td>
                    </tr>
                    <tr>
                        <td>rest/importer</td>
                        <td>POST</td>
                        <td>Adds / alters a configuration for an importer</td>
                        <td>JSON ImporterConfigurationServiceBean</td>
                        <td>&nbsp;</td>
                    </tr>
                    <thead>
                    <tr>
                        <td colspan="5">Skill Service</td>
                    </tr>
                    </thead>
                    <tr>
                        <td>rest/skill/{skillName}/people</td>
                        <td>GET</td>
                        <td>Gets the people with the given {skillName}</td>
                        <td>{minLevel}</td>
                        <td>JSON array ProfileBean</td>
                    </tr>
                    <tr>
                        <td>rest/skill</td>
                        <td>POST</td>
                        <td>Adds a new skill for a user</td>
                        <td>JSON SkillServiceBean</td>
                        <td>&nbsp;</td>
                    </tr>
                    <thead>
                    <tr>
                        <td colspan="5">System Health Service</td>
                    </tr>
                    </thead>
                    <tr>
                        <td>rest/system</td>
                        <td>GET</td>
                        <td>Gets system status / health information</td>
                        <td>&nbsp;</td>
                        <td>JSON SystemHealthServiceBean</td>
                    </tr>
                    <thead>
                    <tr>
                        <td colspan="5">Badge Assertion Service</td>
                    </tr>
                    </thead>
                    <tr>
                        <td>rest/badges/assertion/{namespace}</td>
                        <td>GET</td>
                        <td>Gets badge assertion with the {namespace}</td>
                        <td>&nbsp;</td>
                        <td>JSON BadgeAssertion</td>
                    </tr>
                    <tr>
                        <td>rest/badges/assertion/{namespace}</td>
                        <td>POST</td>
                        <td>Creates a badge assertion with the {namespace}</td>
                        <td>JSON BadgeAssertion</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>rest/badges/assertion/list/{username}</td>
                        <td>GET</td>
                        <td>Gets badge assertions for a user</td>
                        <td>
                            {trustedIssuer} Optional list of issuer URLs<br />
                            {validate} validate the assertions (default false)
                        </td>
                        <td>JSON array BadgeAssertion</td>
                    </tr>
                    <tr>
                        <td>rest/badges/assertion/list/html/{username}</td>
                        <td>GET</td>
                        <td>Gets badge assertions for a user</td>
                        <td>
                            {trustedIssuer} Optional list of issuer URLs
                        </td>
                        <td>HTML badge assertions</td>
                    </tr>
                    <thead>
                    <tr>
                        <td colspan="5">Badge Bakery Service</td>
                    </tr>
                    </thead>
                    <tr>
                        <td>rest/badges/bake/{namespace}</td>
                        <td>GET</td>
                        <td>Bakes a badge image</td>
                        <td>{image} URL of badge image<br />{namespace} Badge namespace</td>
                        <td>image/png</td>
                    </tr>
                    <thead>
                    <tr>
                        <td colspan="5">Badge Class Service</td>
                    </tr>
                    </thead>
                    <tr>
                        <td>rest/badges/class/{namespace}</td>
                        <td>GET</td>
                        <td>Gets a badge class</td>
                        <td>&nbsp;</td>
                        <td>JSON BadgeClass</td>
                    </tr>
                    <tr>
                        <td>rest/badges/class/{namespace}</td>
                        <td>POST</td>
                        <td>Adds a badge class</td>
                        <td>JSON BadgeClass</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>rest/badges/class/{namespace}/people</td>
                        <td>GET</td>
                        <td>Gets people with a specific badge</td>
                        <td></td>
                        <td>JSON array ProfileBean</td>
                    </tr>
                    <thead>
                    <tr>
                        <td colspan="5">Badge Validation Service</td>
                    </tr>
                    </thead>
                    <tr>
                        <td>rest/badges/validate/enrich</td>
                        <td>GET</td>
                        <td>Gets an enriched assertion</td>
                        <td>{badge} Badge URL<br />{trustedIssuer} List of issuer URLs</td>
                        <td>JSON EnrichedBadgeClass</td>
                    </tr>
                    <tr>
                        <td>rest/badges/validate/enrich/{username}</td>
                        <td>GET</td>
                        <td>Gets an enriched assertion for a recipient</td>
                        <td>{badge} Badge URL<br />{trustedIssuer} List of issuer URLs</td>
                        <td>JSON EnrichedBadgeClass</td>
                    </tr>
                    <tr>
                        <td>rest/badges/validate/validate</td>
                        <td>GET</td>
                        <td>Validates a badge</td>
                        <td>{badge} Badge URL<br />{trustedIssuer} List of issuer URLs</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>rest/badges/validate/validate/{username}</td>
                        <td>GET</td>
                        <td>Validates a badge for a recipient</td>
                        <td>{badge} Badge URL<br />{trustedIssuer} List of issuer URLs</td>
                        <td>&nbsp;</td>
                    </tr>
                    <thead>
                    <tr>
                        <td colspan="5">Badge Issuer Service</td>
                    </tr>
                    </thead>
                    <tr>
                        <td>rest/badges/issuer/{namespace}</td>
                        <td>GET</td>
                        <td>Gets an issuer</td>
                        <td>&nbsp;</td>
                        <td>JSON IssuerOrganisation</td>
                    </tr>
                    <tr>
                        <td>rest/badges/issuer/{namespace}</td>
                        <td>POST</td>
                        <td>Gets an issuer</td>
                        <td>JSON IssuerOrganisation</td>
                        <td>&nbsp;</td>
                    </tr>
                    <thead>
                    <tr>
                        <td colspan="5">Badge Revocation List Service</td>
                    </tr>
                    </thead>
                    <tr>
                        <td>rest/badges/revocationlist/{namespace}</td>
                        <td>GET</td>
                        <td>Gets a revocation list</td>
                        <td>&nbsp;</td>
                        <td>JSON RevocationList</td>
                    </tr>
                    <tr>
                        <td>rest/badges/revocationlist/{namespace}</td>
                        <td>POST</td>
                        <td>Adds a revocation list for a badge namespace</td>
                        <td>JSON RevocationList</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>rest/badges/revocationlist/{namespace}/{uid}</td>
                        <td>PUT</td>
                        <td>Revokes a badge</td>
                        <td>{reason}</td>
                        <td>&nbsp;</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
