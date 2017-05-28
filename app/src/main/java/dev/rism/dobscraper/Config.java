package dev.rism.dobscraper;

/**
 * Created by risha on 08-10-2016.
 */
public class Config {
    public static  final String TIH="http://history.muffinlabs.com/date";
    public static final String prefix="https://en.m.wikipedia.org/wiki/";
    public static  final String wiki="https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=original&titles=India";
    public static final String URL="https://dbpedia.org/sparql?default-graph-uri=http://dbpedia.org&query=PREFIX dbo: <http://dbpedia.org/ontology/>PREFIX dbp: <http://dbpedia.org/property/>PREFIX dbpedia: <http://dbpedia.org/resource/>prefix xsd: <http://www.w3.org/2001/XMLSchema#>SELECT ?person  WHERE { ?person  dbo:birthDate '";
    public static final String q2="'^^xsd:date}&format=application/sparql-results+json&timeout=30000&debug=on";
    public  static final String q1="https://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=PREFIX+dbo%3A+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2F%3E%0D%0APREFIX+dbp%3A+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2F%3E%0D%0APREFIX+dbpedia%3A+%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2F%3E%0D%0Aprefix+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3E%0D%0ASELECT+DISTINCT+%3Fperson+%3Fthumbnail+WHERE+%7B+%3Fperson++dbo%3AbirthDate+%27";
    public static  final String q3="%27%5E%5Exsd%3Adate+.%0D%0A+%3Fperson++dbo%3Athumbnail+%3Fthumbnail+%0D%0A%7D&format=application%2Fsparql-results%2Bjson&CXML_redir_for_subjs=121&CXML_redir_for_hrefs=&timeout=30000&debug=on";
}
