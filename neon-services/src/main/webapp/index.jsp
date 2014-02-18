<html>
<body>
<h2>Rest services</h2>
<table border="1">
    <thead>
        <tr>
            <td>Service URL</td>
            <td>HTTP Method</td>
            <td>Description</td>
            <td>Request payload</td>
            <td>Response payload</td>
        </tr>
    </thead>
    <tr>
        <td>rest/profile/{userID}</td>
        <td>GET</td>
        <td>Gets the profile for a user</td>
        <td>{userID}</td>
        <td>JSON representation of com.surevine.neon.model.ProfileBean</td>
    </tr>
    <tr>
        <td>rest/profile</td>
        <td>POST</td>
        <td>Adds a userID to the profile service (data import will run for this user on next scheduled import)</td>
        <td>{userID}</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>rest/profile</td>
        <td>DELETE</td>
        <td>Removes a userID, and all associated profile data (TODO), from the profile service</td>
        <td>{userID}</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>rest/inload</td>
        <td>GET</td>
        <td>Forces a data import for the given userID</td>
        <td>{userID}</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>rest/inload/importerconfig/{importerName}</td>
        <td>GET</td>
        <td>Displays an importer's configuration as JSON</td>
        <td>{importerConfig}</td>
        <td>JSON representation of com.surevine.neon.service.bean.ImporterConfigurationServiceBean</td>
    </tr>
    <tr>
        <td>rest/inload</td>
        <td>POST</td>
        <td>Adds / alters a configuration for an importer</td>
        <td>JSON representation of com.surevine.neon.service.bean.ImporterConfigurationServiceBean</td>
        <td>&nbsp;</td>
    </tr>
</table>
</body>
</html>
