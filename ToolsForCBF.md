# Tools for Content-Based Filtering

In practice, you probably don't want to implement content-based filters yourself much of the time. It's often easier to use an existing tool, such as a search package, to implement the content-based filter.

Some that you may wish to consider:

- [Apache Lucene](http://lucene.apache.org/) is an open-source Java search package that is widely used and has good out-of-the-box search performance.

- [Apache Solr](http://lucene.apache.org/solr/) is built on top of Lucene and provides an HTTP-based API for access from any programming language, scalability features, and much more. It takes Lucene's algorithms and makes them much easier to use.

- [ElasticSearch](https://www.elastic.co/products/elasticsearch) is another search server that provides an HTTP API, like Solr. Most search packages can be repurposed into content-based recommenders.

- [lunr.js](http://lunrjs.com/) implements basic search & content-based filtering algorithms in JavaScript, and can be used client-side in the browser.

- [Xapian](http://xapian.org/) is a GPL-licensed C++ search engine.
Java- or REST-based solutions can be integrated into LensKit for evaluation and web application integration. 
