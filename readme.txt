JGAP is a Genetic Algorithms and Genetic Programming framework written
in Java.

Thank you for trying JGAP! We are very interested in any feedback
you might have, including details of how you're using JGAP, what you
like about it, and what you think needs improvement. You can leave this
feedback on the user's mailing list at jgap-users@lists.sourceforge.net
or, if you prefer to keep these details private, you can email the JGAP
project administrator at klausikm@users.sourceforge.net. The more feedback
we get, the more we can improve JGAP to better meet the needs of our 
users. Thanks for your support!

--------------------------------------------------------------------------

Installation:

Please note that the latest public release of JGAP can always be found
by following the "Download" link on the JGAP home page. The home page 
is located at http://jgap.sourceforge.net.

Step 0 is optional (proceed it in case you download the 3rd-party-zip):

0. Obtain the third party libraries used with JGAP. You get them in
the download section. These files need to be bundles separatedly due
to license issues.

1. If you haven't already, untar or unzip the JGAP archive into your
directory of choice.

2. Simply add the jgap.jar to your classpath. If you wish to use the
examples, then also add the jgap-examples.jar to your classpath.

3. Finally, if you wish to view or modify the source code, it can
be found in the src/ directory of the archive. For convenience, an Ant
build script is included. For more information on Ant, please see the
Ant home page at http://ant.apache.org

4. Javadocs can be found in the doc/ directory (full or javadoc JGAP
distribution). Other documentation, including basic documents to help
you getting started, are put together on the JGAP homepage at
     http://jgap.sourceforge.net
Simple examples which help to get started with JGAP are included under
the examples directory.

--------------------------------------------------------------------------

About This Release:

This represents a major release manifesting the long awaited Genetic
Programming capabilities (see package org.jgap.gp, we promised it).
As always, the javadocs have been improved, of curse new unit tests have
been added, and two new example demonstrate JGAP's GP capabilities.
One of these two examples - Fibonacci calculation - is quite complex and
demonstrates what is possible with JGAP. Output of generated GP programs
as a graphical tree in PNG file format is integrated with JGAP.
The quality of this release is ensured by considering three Release
Candidates.

For a general list of changes, fixes, and enhancements that have been
included in this version, please see the changelog.txt file.

ATTENTION: TO BE ABLE TO COMPILE THE PROVIDED SOURCES (only applicable if
           you downloaded the source distribution of JGAP) YOU NEED TO
           DOWNLOAD THE EXTERNAL JARS AS WELL AND PUT THE EXTRACTED JARS
           INTO YOUR CLASS PATH (in case they are not already contained
           with the distribution you downloaded)

The directory "doc" included with this release contains the documentation
you could find at the JGAP homepage http://jgap.sourceforge.net

This software is OSI Certified Open Source Software. OSI Certified is
a certification mark of the Open Source Initiative.

JGAP documentation, including Javadocs and some basic introductory
documents to help get you started, can be found on the JGAP home page at
http://jgap.sourceforge.net. Javadocs can also be found in the javadocs/
directory of the JGAP distribution, and other documentation can be found
in the docs/ directory of the distribution. For a basic introduction to 
using JGAP, we highly recommend reading the tutorial that can be found 
both on the JGAP website and in the docs/ directory. Finally, a simple
example is also available in the src/examples directory.

JGAP was compiled and tested with Java 1.4 and Java 1.5 on
Windows 2000 and XP. Prior versions of JGAP have been tested with Linux
and MacOS as well.

This release contains code that is based on external libraries. You could
find it in the src-3rdparty library. Due to licensing issues the needed
external libraries (such as Colt) are not included with JGAP you need to
download them separately. The links to the download sites could be found
in the header comments of the corresponding JGAP source files.

Please direct any questions, comments, problems, or inquiries to the
JGAP users mailing list at:

    jgap-users@lists.sourceforge.net

or to the JGAP developers mailing list at jgap-devl@lists.sourceforge.net.

Public announcements concerning JGAP, including any new release
announcements, are made on the jgap-announce mailing list. If you would
like to subscribe to this list, or to any other JGAP list, please visit 
the JGAP mailing list manager at:

    http://sourceforge.net/mail/?group_id=11618

Mailing list archives are also available for each of the lists.

Thanks again for trying out JGAP!
--Klaus Meffert for the JGAP team

-----------------------------------------------

This product includes software developed by the
Apache Software Foundation (http://www.apache.org/).
For further licensing information about all packages included with JGAP
please see the directory "licenses".
