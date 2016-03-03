# Wikidata Toolkit Examples

Here, I memo my current use of these examples.

What's found in this repository
-------------------------------

I copy local dumps here:

../data/WikiData/dumpfiles/wikidatawiki

or better to 

/Volumes/data2/WikiData

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