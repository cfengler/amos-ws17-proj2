package de.tuberlin.amos.ws17.swit.demo;

import de.tuberlin.amos.ws17.swit.common.GpsPosition;
import de.tuberlin.amos.ws17.swit.common.PointOfInterest;
import de.tuberlin.amos.ws17.swit.information_source.WikiAbstractProvider;

public class AbstractProviderTester {


    public static void main(String[] args) {
        WikiAbstractProvider wp = new WikiAbstractProvider();
        PointOfInterest a = new PointOfInterest("", "post hoc ergo propter hoc", new GpsPosition(5.5, 6.6));
        PointOfInterest b = new PointOfInterest("", "gibberish", new GpsPosition(5.5, 6.6));
        PointOfInterest c = new PointOfInterest("", "ghoti", new GpsPosition(5.5, 6.6));

        System.out.println(wp.provideAbstract(a).getInformationAbstract());
        System.out.println("\n\n" + wp.provideAbstract(b).getInformationAbstract());
        System.out.println("\n\n" + wp.provideAbstract(c).getInformationAbstract());
    }
}