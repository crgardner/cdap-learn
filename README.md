cdap-learn
==========
###Overview
cdap-learn is a sandbox of code to explore and learn the [Cask Data Application Platform.](http://cdap.io/) 

The sandbox includes the songs project, which does the following: 
  - Creates a flow to which song titles are streamed
  - Filters song titles with the word "love"
  - Captures and reports statistics on the number of filtered songs

###Statistics Report
The report has the following elements:
  - _loveSongs_ - total number of love songs
  - _lotsOfLove_ - total number of love songs with the word "love" appearing at least twice in the title (_i.e._, "lots of love")
  - _minusculeLove_ - total number of love songs with the word "love" appearing only once (_i.e._, minuscule love).

###Technical Notes
  - Uses CDAP 2.5.2
  - Requires Java 7 and above
