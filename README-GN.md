# Wikidata Toolkit Examples

Here, I memo my current use of these examples.

What's found in this repository
-------------------------------

I copy local dumps here:

../data/WikiData/dumpfiles/wikidatawiki

or better to 

/Volumes/data2/WikiData

but results on

../data/WikiData/results/

Adapt code in ExampleHelpers

Günnies Experiments
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

Ok, it seems to be ok to process one WikiData dump and extract information.

Now, what information should I extract ? And then ?

Adding Wikipedia page extraction
--------------------------------

I installed via Maven the following package:
https://bitbucket.org/axelclk/info.bliki.wiki/wiki/Home

It helps to extract plain text from Wikipedia pages.

I will try it out. Seems to work: gntests/Wikipedia2Txt.java

Code based on http://trulymadlywordly.blogspot.de/2011/03/creating-text-corpus-from-wikipedia.html

Processing latest-dump for DE-Wikipedia
---------------------------------------

Processing took some hours on my computer, but because computer slept over night.

Processing data:
- Input: 	data/WikiPedia/dewiki-latest-pages-articles.xml.bz2
- Output: 	dewiki-latest-pages-articles.txt
			

- resulted in processing of about 
	Pages: 	  3,230,000
	Sents: 	 43,600,000
	Tokens:	643,764,664
	
- output of wc command
	43631144 643764664 4658296212 dewiki-latest-pages-articles.txt

- Size: about 4,7 GB uncompressed, 1,79 compressed
