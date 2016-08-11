# Wikidata Toolkit Examples

Here, I memo my current use of these examples.

TESTIX

What's found in this repository
-------------------------------

Dumps can be downloaded from here:

https://dumps.wikimedia.org/enwiki/latest/

I copy local dumps here:

../data/WikiData/dumpfiles/wikidatawiki

or better to 

/Volumes/data2/WikiData

but results on

../data/WikiData/results/

Adapt code in ExampleHelpers

# Günnies Experiments
-------------------------------

set 
	examples.ExampleHelpers.OFFLINE_MODE = true;
	
to make sure local dumps are used.


********************************************************************
*** Wikidata Toolkit: JsonSerializationProcessor
*** 
*** This program will download and process dumps from Wikidata.
*** It will filter the data and store the results in a new JSON file.
*** See source code for further details.
********************************************************************
2016-03-03 15:40:30 INFO  - Using download directory /Users/gune00/git/Wikidata-Toolkit-Examples/dumpfiles/wikidatawiki
2016-03-03 15:40:30 INFO  - Found 1 local dumps of type JSON: [wikidatawiki-json-20150420]
2016-03-03 15:40:30 INFO  - Processing JSON dump file wikidatawiki-json-20150420
2016-03-03 15:40:30 INFO  - Starting processing.
...
2016-03-03 15:52:51 INFO  - Finished processing.
2016-03-03 15:52:51 INFO  - Processed 17637168 entities in 739 sec (23866 per second)
Serialized 1911 item documents to JSON file json-serialization-example.json.gz.

Running LocalDUmpFileExample.java with "/Volumes/data2/WikiData/latest-all.json.gz":

Processing a local dump file with all meta-data set:
2016-08-11 10:04:15 INFO  - Processing JSON dump file /Volumes/data2/WikiData/latest-all.json.gz (wikidatawiki/json/20150815)
2016-08-11 10:25:37 INFO  - Processed 46508138 entities in 2611 sec (17812 per second)

Processing gntests.WikiDataExtractor.main(String[]) with "/Volumes/data2/WikiData/latest-all.json.bz2":
NOTE: it means using external USB2 device.
Start at: 10:35:02

# Adding Wikipedia page extraction

I installed via Maven the following package:
https://bitbucket.org/axelclk/info.bliki.wiki/wiki/Home

It helps to extract plain text from Wikipedia pages.

I will try it out. Seems to work: gntests/Wikipedia2Txt.java

Code based on http://trulymadlywordly.blogspot.de/2011/03/creating-text-corpus-from-wikipedia.html

# Processing latest-dump for DE-Wikipedia

Processing took some hours on my computer, but because computer slept over night.

Processing data:
- Input: 	data/WikiPedia/dewiki-latest-pages-articles.xml.bz2
- Output: 	data/WikiPedia/dewiki-latest-pages-articles.txt
			

- resulted in processing of about 
	Pages: 	  3,230,000
	Sents: 	 43,600,000
	Tokens:	643,764,664
	
- output of wc command
	43631144 643764664 4658296212 dewiki-latest-pages-articles.txt

- Size: about 4,7 GB uncompressed, 1,79 compressed

# Processing latest-dump for EN-Wikipedia

Processing time on my MacBookPro:
real	406m26.239s -> about 6,76667 hours
user	261m4.480s
sys		90m53.692s

Processing data:
- Input: 	data/WikiPedia/enwiki-latest-pages-articles.xml.bz2
- Output: 	data/WikiPedia/enwiki-latest-pages-articles.txt
			

- resulted in processing of about 
	Pages: 		9,880,000
	Sents: 	  108,800,000
	Tokens:	1,961,587,703

- output of wc
	108826743 1961587703 11878221225 enwiki-latest-pages-articles.txt

- Size: about 11,9 GB uncompressed, 4,37 compressed

Trial run
---------

/Users/gune00/data/WikiPedia/enwiki-latest-pages-articles.xml.bz2
/Users/gune00/data/WikiPedia/enwiki-latest-pages-articles-test2.txt

Create BZ2 /GZ output file
-----------------------

Still to do, but see 
[Oracle document] (http://www.oracle.com/technetwork/articles/java/compress-1565076.html)


# First simple WikiData dump reader
---------------------------------------
/wdtk_examples/src/gntests/WikiDataExtractor.java

* processes local dump
* creates a jackson object for each line and counts lines
* extracts field "id"
* basically the same as /wdtk_examples/src/examples/JsonSerializationProcessor.java