#Shoptimize

Shoptimize is an application designed to make your trips to the grocery store as easy and efficient as possible. We expect to do this by allowing users to create and edit multiple shopping lists for each and every store that they plan to go to, store and manage coupons, and find the locations of each item in their list is in the store to minimize time spent and reduce impulse purchases. 

**Installation**

Navigate to http://www-edlab.cs.umass.edu/~pfarlow/ on an Android device (either physical or emulated) to download and install Shoptimize.

**Source Control and Build Instructions**

As the development team for Shoptimize, we must take certain steps to ensure that  Shoptimize is in a runnable state at all times, and that all team members are up to date on the state of the app. Our team works to keep Shoptimize bug-free and smoothly running. Our team constantly tests changes to the code, and if an issue arises, they are notified using Git's issue tracking system. A majority of the work for the app has been done in a group setting as people need to get accustomed to using Android Studio and Git and being in a group allows us to help each other with problems.

The code for Shoptimize is being shared with all team members via the source control tool known as "Git". At any time, any team member can access the Git repository and see what status the repository is in. This includes any recent changes to the repository, any discussions on the project, and any reported bugs that have occurred. Using Git, any team member can make changes to the code, and these changes will be recorded in Git's robust history system.

1. Getting the project files
  * If you haven't already, install git from http://git-scm.com/.
  * In Git bash, navigate to a new project folder and run the command "git init".
  * Run the command "git clone https://github.com/gmarkv10/Shoptimize.git".

2. Setting up Android Studio
  * Ensure you have Java JDK version 7u75 installed. If you don't, install it from http://www.oracle.com/technetwork/java/javase/downloads/index.html and then restart your computer.
  * If you want to be able to run the app from Android Studio’s built in emulator, make sure that virtualization is enabled in your BIOS settings
  * Go to http://developer.android.com/sdk/index.html and install Android Studio.
  * Open Android Studio and select "import project". Specify the directory where you cloned the source files from Github.
     

3. Building the project
 
 ![alt tag](https://github.com/gmarkv10/Shoptimize/blob/master/ShoptimizeApp/app/src/main/res/drawable/readme_build_image.png) 

  * As seen in this picture, make sure that the ‘1: Project’ tab is selected on the left side of Android Studio and that the drop down menu directly next to it reads ‘Project Files’.
  * b.	Once you’ve imported the project and opened the project files view, open the Build menu and select ‘Make project’. After Java, Git, and Android Studio are set up, building our project is as easy as running ‘rebuild’ from this menu.

**Change Logs**

Github has a commit history that documents every change ever made to the repository. Looking at the commit history and the descriptions of each commit makes it easy to track the history of the project and any design choices that have been made in the project. 

**Issue Tracking**

Bugs encountered while using Shoptimize are recorded in Git's issue tracking system. Since the Git repository is public, any person with a GitHub account may open issues in the issue tracker and discuss issues already in existence. Collaborators may open, close, edit, and assign people to issues. Collaborators also get a notification if a new issue has been opened or a pre-existing issue has been closed in the issue system.

To access the issue tracking system for Shoptimize, navigate to https://github.com/gmarkv10/Shoptimize/issues in your browser. Doing anything other than just viewing issues requires a GitHub account.

* To open an issue, click on the “new issue” button and fill out a new issue
* To view and/or discuss an existing issue, click on one of the issues.

**Testing Plan**

All instructions for the tests that we have can be found here.
https://github.com/gmarkv10/Shoptimize/wiki/Running-Tests
