package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
        parseRawReviewLine();
    }

    public void parseTopicName() {
        String s = "<a href=\"https://www.cochranelibrary.com/en/search?p_p_id=scolarissearchresultsportlet_WAR_scolarissearchresults&p_p_lifecycle=0&\n" +
                "p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&\n" +
                "_scolarissearchresultsportlet_WAR_scolarissearchresults_displayText=Allergy+%26+intolerance&\n" +
                "_scolarissearchresultsportlet_WAR_scolarissearchresults_searchText=Allergy+%26+intolerance&\n" +
                "_scolarissearchresultsportlet_WAR_scolarissearchresults_searchType=basic&\n" +
                "_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryField=topic_id&\n" +
                "_scolarissearchresultsportlet_WAR_scolarissearchresults_searchBy=13&\n" +
                "_scolarissearchresultsportlet_WAR_scolarissearchresults_orderBy=displayDate-true&\n" +
                "_scolarissearchresultsportlet_WAR_scolarissearchresults_facetDisplayName=Allergy+%26+intolerance&\n" +
                "_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryTerm=z1506030924307755598196034641807&\n" +
                "_scolarissearchresultsportlet_WAR_scolarissearchresults_facetCategory=Topics\">\n" +
                "<button class=\"btn-link browse-by-list-item-link\">Allergy & intolerance</button></a>";
    }

    public void parseRawReviewLine() {
        String raw = "<div class=\"search-results-item\">\n" +
                " <div class=\"search-results-item-tools\">\n" +
                " <div> <label for=\"exportCD013534.PUB3\">1</label>\n" +
                " <input type=\"checkbox\" name=\"exportCD013534.PUB3\" id=\"exportCD013534.PUB3\" value=\"CD013534.PUB3\" />\n" +
                " </div> </div>\n" +
                " <div class=\"search-results-item-body\">\n" +
                " <h3 class=\"result-title\" >\n" +
                " <a target=\"_blank\" href=\"/cdsr/doi/10.1002/14651858.CD013534.pub3/full\">\n" +
                "    Skin care interventions in infants for preventing eczema and food allergy\n" +
                " </a> </h3>\n" +
                " <div class=\"search-result-authors\">\n" +
                " <div>Maeve M Kelleher, Rachel Phillips, Sara J Brown, Suzie Cro, Victoria Cornelius, Karin C Lødrup Carlsen, Håvard O Skjerven, Eva M Rehbinder, Adrian J Lowe, Eishika Dissanayake, Naoki Shimojo\n" +
                " <sup>a</sup>\n" +
                " , Kaori Yonezawa, Yukihiro Ohya, Kiwako Yamamoto-Hanada, Kumiko Morita, Emma Axon, Michael Cork, Alison Cooke, Eleanor Van Vogt, Jochen Schmitt, Stephan Weidinger, Danielle McClanahan, Eric Simpson, Lelia Duley, Lisa M Askie, Hywel C Williams, Robert J Boyle\n" +
                " </div> </div>\n" +
                " <div class=\"search-result-metadata-block\">\n" +
                " <div class=\"search-result-metadata-item\" style=\"display: none;\" >\n" +
                " <div class=\"access\" data-article-doi=\"10.1002/14651858.CD013534.pub3\">\n" +
                " <div class=\"access-icon\"> <i class=\"ai ai-open-access\"></i> </div> <div class=\"access-label\"></div> </div> </div>\n" +
                " <div class=\"search-result-metadata-item\">\n" +
                " <div class=\"search-result-type\">\n" +
                " <div aria-label=\"A systematic review of studies assessing an intervention for a health problem.\" class=\"custom-tooltip\"\n" +
                "    title=\"A systematic review of studies assessing an intervention for a health problem.\">\n" +
                " Intervention </div> </div> </div>\n" +
                " <div class=\"search-result-metadata-item\">\n" +
                " <div class=\"search-result-stage\">\n" +
                " <div aria-label=\"A full review, complete with results and discussion, possibly including meta-analyses to\n" +
                " combine results across studies.\" class=\"custom-tooltip\"\n" +
                " title=\"A full review, complete with results and discussion, possibly including meta-analyses to combine results across studies.\">\n" +
                " Review </div> </div> </div>\n" +
                " <div class=\"search-result-metadata-item\">\n" +
                " <div class=\"search-result-date\">\n" +
                " <div> 14 November 2022 </div>\n" +
                " </div> </div>\n" +
                " <div class=\"search-result-metadata-item\">\n" +
                " <div class=\"search-result-publication-status\">\n" +
                " <ul class=\"inline-list\">\n" +
                " <li class=\"status-item custom-tooltip\" data-status=\"A new search for studies has been conducted.\"\n" +
                " title=\"A new search for studies has been conducted.\"> New search\n" +
                " </li>\n" +
                " <li class=\"status-item custom-tooltip\" data-status=\"There has been an important change to the conclusions of the review.\"\n" +
                " title=\"There has been an important change to the conclusions of the review.\"> Conclusions changed </li> </ul> </div> </div>\n" +
                " <div class=\"mobile-only search-result-metadata-item\"> </div>\n" +
                " <div class=\"search-result-metadata-block\">\n" +
                " <div class=\"search-result-metadata-item\" id=\"searchResultPicosToggle1\" style=\"display: none\">\n" +
                " <a href=\"#0\" class=\"show-picos-btn\" style=\"display: inline;\"> <strong> Show PICOs <i class=\"fa fa-caret-down\"></i> </strong> </a>\n" +
                " <a href=\"#0\" class=\"hide-picos-btn\" style=\"display: none;\"> <strong> Hide PICOs <i class=\"fa fa-caret-up\"></i> </strong> </a> </div>\n" +
                " <div class=\"search-result-metadata-item\">\n" +
                " <a href=\"#0\" class=\"show-preview-btn\" style=\"display: inline;\"> <strong>Show preview <i class=\"fa fa-caret-down\"></i></strong> </a>\n" +
                " <a href=\"#0\" class=\"hide-preview-btn\" style=\"display: none;\"> <strong>Hide preview <i class=\"fa fa-caret-up\"></i></strong> </a> </div> </div>\n" +
                " <div class=\"update-status-extra-content-wrapper\" style=\"display: none;\"></div>\n" +
                " <div class=\"search-result-picos picos-hidden\" id=\"picoMetadataPanel1\" data-search-result-toggle-id=\"searchResultPicosToggle1\"\n" +
                " data-widget=\"pico-data\" data-doi=\"10.1002/14651858.CD013534.pub3\" data-enable-links=\"false\" data-portal-lang=\"en\"></div>\n" +
                " <div class=\"search-result-preview\" style=\"display: none;\" > <strong>Abstract - Background</strong>\n" +
                " <p>Eczema and food allergy are common health conditions that usually begin in early childhood and often occur in the same people.\n" +
                " They can be associated with an impaired skin barrier in early infancy. It is unclear whether trying to prevent or reverse an impaired\n" +
                " skin barrier soon after birth is effect…</p> </div> </div> </div> </div>";
        String reviewMark = "a target=\"_blank\" href=\"";
        int reviewMarkIndex = raw.indexOf(reviewMark);
        while (reviewMarkIndex >= 0) {
            System.out.println(reviewMarkIndex);
            int indexOfQuote = raw.indexOf("\"", reviewMarkIndex + reviewMark.length());
            String href = raw.substring(reviewMarkIndex + reviewMark.length(), indexOfQuote);
            System.out.println(href);
            raw = raw.substring(reviewMarkIndex + 1);
            reviewMarkIndex = raw.indexOf(reviewMark);
        }
    }
}
