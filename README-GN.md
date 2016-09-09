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

# Useful Other Links

http://linguatools.org/tools/corpora/wikipedia-monolingual-corpora/

# Handling unicode in regexpr

http://jrgraphix.net/r/Unicode/0900-097F
http://www.regular-expressions.info/unicode.html#prop

# HINDI/Korean/Russia/Bulgarian

https://de.wikibooks.org/wiki/Devanagari:_Verschiedenes#Satzzeichen

http://www.utf8-chartable.de/unicode-utf8-table.pl?start=2304&number=128

I solved by using a lnguage flag form the wikipedia dump file name and UTF extensions for Java reg expr

#Russia

real    242m8.505s
user    237m30.349s
sys     12m27.707s

Pages: 2750000

bzcat text/ruwiki-latest-pages-articles.txt.bz2 | wc
21097679 287142597 3782566803

# Korean

bzcat kowiki-latest-pages-articles.txt.bz2 | wc
 3541361 56825897 485005016
 
# Günnies Experiments


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
23250000 entities -> index via "id"
End at:	11:35

# Adding Wikipedia page extraction

I installed via Maven the following package:
https://bitbucket.org/axelclk/info.bliki.wiki/wiki/Home

It helps to extract plain text from Wikipedia pages.

I will try it out. Seems to work: gntests/Wikipedia2Txt.java

Code based on http://trulymadlywordly.blogspot.de/2011/03/creating-text-corpus-from-wikipedia.html

Create BZ2 /GZ output file
-----------------------

Still to do, but see 
[Oracle document] (http://www.oracle.com/technetwork/articles/java/compress-1565076.html)
Done: gntests.Wikipedia2Txt.getBufferedWriterForTextFile(String)

View files with bzcat 

Uncompress with (preserving bz2 file):
bzip2 -dk  <filename.bz2

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

# Processing latest-dump for FR-Wikipedia

/Users/gune00/data/WikiPedia/frwiki-latest-pages-articles.xml.bz2
/Users/gune00/data/WikiPedia/frwiki-latest-pages-articles.txt.bz2

-> at pages 730000 the extractor seems to loop, so had to cancel
Status so far:

 wc frwiki-latest-pages-articles.txt
 9893619 182051605 1170755726 frwiki-latest-pages-articles.txt
 -> Means: missing about 50%

# Processing Danish latest-dump for DA-Wikipedia

Ok, works without problem
wc dawiki-latest-pages-articles.txt 
 2564059 40883286 267532409 dawiki-latest-pages-articles.txt


# Processing Hungarian latest-dump for HU-Wikipedia

Took on lns-87009 from/to gfs-neumann: real    30m38.819s

bzcat huwiki-latest-pages-articles.txt.bz2 | wc
5118735 70951004 575090000



# Processing Turkish latest-dump for TR-Wikipedia

Took on lns-87009 from/to gfs-neumann: real    18m26.591s

bzcat trwiki-latest-pages-articles.txt.bz2 | wc
3199756 41101227 342458051


Then:
run this on all 22 relevant Wikipedia sites for which I have UD languages 

-> it is now clear how to do it



# First simple WikiData dump reader
---------------------------------------
/wdtk_examples/src/gntests/WikiDataExtractor.java

* processes local dump
* creates a jackson object for each line and counts lines
* extracts field "id"
* basically the same as /wdtk_examples/src/examples/JsonSerializationProcessor.java