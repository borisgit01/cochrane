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
        sanitizeString();
    }

    public void sanitizeString() {
        String s = "Naoki Shimojo<sup>a</sup>, Kaori Yonezawa, Yukihiro Ohya, Kiwako Yamamoto-Hanada, Kumiko Morita";
        String noTags = s.replaceAll("<[^>]*>", "");
        assertEquals("Naoki Shimojoa, Kaori Yonezawa, Yukihiro Ohya, Kiwako Yamamoto-Hanada, Kumiko Morita", noTags);
    }

}
