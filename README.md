# Spotify Visualiser DataSource

[![Version][download-version]][maven-url]
[![Downloads Stats][maven-image]][downloads-url]

A library used to filter the raw data from the Spotify APIs for use throughout Visualiser for Spotify. This is done by POJOs which only concerns useful information with respect to the interests of the Visualiser and all other information is not persistant. A set of DAOs which store and retrieve these POJOs in MongoDB are also included. Can be modified for own project use.

## Installation

OS X, Windows & Linux:

```sh
mvn clean install
```

## Usage example

WIP
_For more examples and usage, please refer to the [Wiki][wiki]._ 

## Release History

* 1.1.1
    * CHANGE: Cleanup of aggregation functions.
    * ADD: Aggregation functions.
* 1.1.0
    * FIX: No offset and limit on get popular objects.
    * CHANGE: General project refactoring.
    * FIX: Incorrect limit logic.
* 1.0.9
    * ADD: Grab random track from DB.
    * CHANGE: Update pom versions.
* 1.0.8
    * FIX: No offset and limit on get popular objects.
    * ADD: SCM tidbits.
    * FIX: The child project maven xml file version.
* 1.0.7
    * FIX: Fix the package name.
    * FIX: The child project maven xml files.
* 1.0.6
    * CHANGE: Change package name to align with the rest of the project.
    * CHANGE: General project refactoring.
* 1.0.5
    * ADD: License.
    * ADD: Batch DAO.
* 1.0.4
    * ADD: Completed DAO methods.
    * ADD: Additional tests added.
* 1.0.3
    * ADD: Add development connection.
    * FIX: Tests condition.
* 1.0.2
    * CHANGE: Rename DAOs to match better description.
    * ADD: Another integration test.
* 1.0.1
    * FIX: Nullpointer exception not being caught in the mapper.
* 1.0.0
    * Intial Release.
* 0.0.1
    * Work in progress.

## Meta

Alastair Miller – [LinkedIn](https://www.linkedin.com/in/alastairmiller/) – alastair@miller.al

Distributed under the Apache 2.0 license. See ``LICENSE`` for more information.

[https://github.com/AlastairMiller](https://github.com/AlastairMiller)

## Contributing

1. Fork it (<https://github.com/AlastairMiller/Spotify-Visualiser-DataSource/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

<!-- Markdown link & img dfn's -->
[maven-image]: 	https://img.shields.io/github/downloads/atom/atom/total.svg
[downloads-url]: https://bintray.com/alastairmiller/Spotify-Visualiser-DataSource/Spotify-Visualiser-DataSource/statistics/total_downloads
[maven-url]: https://bintray.com/alastairmiller/Spotify-Visualiser-DataSource/Spotify-Visualiser-DataSource/_latestVersion
[download-version]: https://api.bintray.com/packages/alastairmiller/Spotify-Visualiser-DataSource/Spotify-Visualiser-DataSource/images/download.svg?style=flat-square
[wiki]: https://github.com/AlastairMiller/Spotify-Visualiser-DataSource/wiki
