<img src="https://cloud.githubusercontent.com/assets/668093/12567089/0ac42774-c372-11e5-97eb-00baf0fccc37.jpg" alt="OpenMRS"/>

# Messages OWA Module

This repository contains the messages OpenMRS Open Web App.

For further documentation about OpenMRS Open Web Apps see
[the wiki page](https://wiki.openmrs.org/display/docs/Open+Web+Apps+Module).

## Development

### Production Build

You will need NodeJS 6+ installed to do this. See the install instructions [here](https://nodejs.org/en/download/package-manager/).

Once you have NodeJS installed, install the dependencies (first time only):

```sh
npm install
```

Build the distributable using [Webpack](https://webpack.github.io/) as follows:

````sh
npm run build:prod
````

This will create a file called `messages.zip` file in the `dist` directory,
which can be uploaded to the OpenMRS Open Web Apps module.

### Local Deploy

To deploy directly to your local Open Web Apps directory, run:

````
npm run build:deploy
````

This will build and deploy the app to the `/home/user/.clf-dev/owa`
directory. To change the deploy directory, edit the `LOCAL_OWA_FOLDER` entry in
`config.json`. If this file does not exists, create one in the root directory
that looks like:

```js
{
  "LOCAL_OWA_FOLDER": "/home/user/.clf-dev/owa"
}
```

Note: make sure that you have write access to LOCAL_OWA_FOLDER and its content.

### Live Reload with docker

1. Adjust variables listed below to your environment, open terminal and run them
```bash
MESSAGES_REPO=/home/user/cfl/openmrs-module-messages
MESSAGES_OMOD=messages-1.0.0-SNAPSHOT.omod
CFL_REPO=/home/user/cfl/cfl-openmrs
```
2. Build the messages module
```bash
cd $MESSAGES_REPO
mvn clean install
```
3. Replace the messages module file `CFL_REPO/cfl/web/cfl-modules/MESSAGES_OMOD` by `MESSAGES_REPO/omod/target/MESSAGES_OMOD`
```bash
rm -f $MODULES_PATH/messages*
mv $MESSAGES_REPO/omod/target/$CALLFLOWS_OMOD $MODULES_PATH
```
4. Run docker-compose
```bash
cd $CFL_REPO/cfl/
docker-compose down
docker-compose up -d --build
```
5. Log in to local OpenMRS app to make sure everything works fine before executing further steps.

6. (Optional) if your system account have different name than 'user' go to `MESSAGES_REPO/owa` and create a file `config.json`
```bash
cd $MESSAGES_REPO/owa
touch config.json
```
replace `<YOUR_USERNAME>` with our own username and paste into `config.json` file.
```js
{
  "LOCAL_OWA_FOLDER":"/home/<YOUR_USERNAME>/.clf-dev/owa",
  "APP_ENTRY_POINT":"http://localhost:8080/openmrs/owa/messages/index.html"
}
```
This file isn't tracked by git so you can leave it like that.

7. Now run
```bash
sudo chown -R $(whoami):$(whoami) $OWA_PATH
cd $MESSAGES_REPO/owa
npm run watch
```

8. You can add
```html
<h1>TEST</h1>
```
to `MESSAGES_REPO/owa/js/components/App.jsx` in order to check if it works.

<b>Note!</b>
* You will have to use `Ctrl+F5` to see changes in the HTML. It's caused by Docker volume system.
* You can use the script `runNpmWatch.sh` which checks permissions and runs `npm run watch`.
* After rerunning of OpenMRS (`run.sh`), you should also rerun the `runNpmWatch.sh` script.
  * run.sh and runNpmWatch.sh require adding your current user to the docker group (permission issues) - https://docs.docker.com/install/linux/linux-postinstall/

### Extending

Install [npm](http://npmjs.com/) packages dependencies as follows:

````sh
npm install --save <package>
````

To use the installed package, import it as follows:

````js
//import and assign to variable
import variableName from 'package';
````

To contain package in vendor bundle, remember to add it to vendor entry point array, eg.:

````js
entry: {
  app : `${__dirname}/app/js/owa.js`,
  css: `${__dirname}/app/css/owa.css`,
  vendor : [
    'package',
    ...//other packages in vendor bundle
  ]
},
````

Any files that you add manually must be added in the `app` directory.

### Troubleshooting

##### [HTTP access control (CORS)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS)

You may experience problems due to the `Access-Control-Allow-Origin` header not
being set by OpenMRS. To fix this you'll need to enable Cross-Origin Resource
Sharing in Tomcat.

See instructions [here](http://enable-cors.org/server_tomcat.html) for Tomcat 7 and [here](https://www.dforge.net/2013/09/16/enabling-cors-on-apache-tomcat-6/) for Tomcat 6.

## License

[MPL 2.0 w/ HD](http://openmrs.org/license/)
