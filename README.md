LineageTracker Export Plugin
============================

Summary
-------

This is a Plugin for [LineageTracker](http://www2.warwick.ac.uk/fac/sci/systemsbiology/staff/bretschneider/lineagetracker) which can export entire lineages into either CSV or JSON data formats.

Binary Installation
-------------------

The following assumes that you have a working installation of [FijI](http://fiji.sc/) and [LineageTracker](http://www2.warwick.ac.uk/fac/sci/systemsbiology/staff/bretschneider/lineagetracker). The minimum required version of LineageTracker is 1.2.3.

1. JSON export depends on [JSON-Simple](https://code.google.com/p/json-simple/). Here is 
   a link to the JAR file you will need: [http://json-simple.googlecode.com/files/json-simple-1.1.1.jar](http://json-simple.googlecode.com/files/json-simple-1.1.1.jar)
   Download this file, and copy it into the FijI/ImageJ plugin directory.
2. Download [LineageTracker_JSONExport.jar](binaries/LineageTracker_JSONExport.jar), and copy it into the FijI/ImageJ plugin directory.

Usage
-----

### Validating Lineages

When tracking a lineage is completed, select the first cell, and click 'Validate Cell (v)' to validate the links and divisions. The lineage will then be recognised by this plugin and included in exported files.

### Actions

1. _Clicking Cells:_ When this plugin is active, clicking cells will toggle the validation status for the whole lineage. This allows to 'invalidate' lineages and remove them from the exported set. 
2. _Setup Plugin:_ Shows a dialog for previewing and filtering the set of lineages which will be exported.
3. _Run Plugin:_ This will run the export. Select a data format (JSON or CSV) and a file name. The log window will show summary information about the exported data.

### Output Format

The JSON output format is described in See [docs/example.json](docs/example.json). CSV files contain a column for each timepoint, and a set of rows for each channel for each cell in the dataset.

Building and Debugging
----------------------

See [docs/Development.md](docs/Development.md)