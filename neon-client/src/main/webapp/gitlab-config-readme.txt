Gitlab Integration Configuration

In order to replace the standard gitlab profile pages with the neon profiles,
following these steps:

1) SSH onto gitlab host
2) Edit the following file:

   /path/to/gitlab/apache2/conf/bitnami/bitnami.conf 
   
3) Add the following Rewrite commands at line 25 (inside _default:80 VirtualHost):

   RewriteEngine on
   RewriteRule ^/u/(.*)$ http://10.66.2.126:8080/neon-client/#/profile/$1 [R=302,NE,L]
   
4) Restart apache.

To test, navigate to a user profile in Gitlab (/u/username), and you should be
forwarded to the neon profile page for the same user.

In order to allow users to use the neon-client's heading links back to Gitlab,
you will need to apply some configuration on the neon-client:

1) SSH onto the neon-client host
2) Edit the following file:

   /path/to/neon-client/js/config.js
   
3) Update the value of the 'gitlabBaseUrl' property to be the Gitlab installation's URL
4) Restart web server