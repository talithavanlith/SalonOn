#SalonON

_About:_

This project serves as the android app prototype for the stylist booking platform SalonOn. The project is developed for the COMP523 Software Engineering course at the University of North Carolina at Chapel Hill. This repository contains the frontend android source code. For the server side repo, see https://github.com/tmam101/salon-on-backend/branches

_Summary:_

SalonON is a stylist booking platform designed with convenience in mind. The app aims to be a 3 tiered solution for clients, stylist, and salons, allowing Customers to be able to create profiles detailing their hair type, style interests, and location preferences. All of which is then used to match them with the perfect stylist in their area. The stylist, with a profile of their own, can specify their experience, showcase their work via a photo album, and contact salons with space available to set the appointment, all from within the app. 

_Getting Started:_

This project is developed using Android Studio IDE: https://developer.android.com/studio. For a suitable developing environment,it is recommended to use the IDE in conjunction with a mobile device running Android 8.0 or later, however, a built in emulator is available as an alternative. If using a physical device, Android SDK Platform tools is required. https://developer.android.com/studio/releases/platform-tools. Once the SDK is installed, navigate to developer options on the mobile device and enable USB debugging. You should now be able to run the command 'adb devices' and see a list containing the connected Android device. For troubleshoting adb device issues see https://developer.android.com/studio/command-line/adb.

_Testing:_

Currently the unit test suite for the networking classes can be found in the java directory (when 'viewing project' mode from the IDE) or from app/src/test/java/com/example/salonon/. The tests can be run by right clicking on a test class and clicking run. 

_Deployment:_

The codebase of this project lives on two repositories. This one, and the backend mentioned in the about section. While the backend is hosted by heroku, the android app is still in development and as such the frontend has not been officially deployed to any platform at the time of this documentation. However a walking skeleton of the app can be accessed from the project website located at https://frosty-tereshkova-9806e1.netlify.com/index.html.

_Technologies Used:_

The technologies actively the project include the Android Platform via Java as an environment, Google Maps for improved location data, Amazon Web Services RDS as a database solution, Jest testing framework, and Heroku Cloud Platform for hosting the backend server.

Google Maps: https://developers.google.com/maps/documentation/android-sdk/intro

AWS: https://aws.amazon.com

Jest: https://jestjs.io

Heroku: https://www.heroku.com

_Contributing:_

Aside from GitHub, the team utilizes a Trello board for project management as well as engages in weekly face to face meetings. More information on the team, and the style guides used can be found on the project website under the 'Team' section.

_Authors:_

Ethan Bateman - ethanB@unc.edu - Backend Developer and Database Admin. 
Key responsibilities have included designing the schema for the SQL database, implementing the database communication with 
the Node.js server, and structuring the android classes for storing data locally.

David Moore - d42n81@live.unc.edu - Frontend Developer and Communications Head
Responsible for integrating Android UI components, specifying the API interface requirements from the backend, and  coordinating schedules between the team, client, and team mentor.

Talitha Van Lith - talitha@live.unc.edu - Frontend Developer, Webmaster and Lead Designer
Tasked with producing a clickable prototype, designing Android UI elements, integrating google maps functionality, and managing the project website.

_License:_
TBD

_Acknowledgements:_

We extend our graditude to our mentor Mike Lake, and Professor Jeff Terrel for providing their insights throughout the duration of this project.



