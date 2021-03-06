package de.tuberlin.amos.ws17.swit.poi.google;

import de.tuberlin.amos.ws17.swit.common.exceptions.ModuleNotWorkingException;
import de.tuberlin.amos.ws17.swit.common.exceptions.ModuleViolationException;
import de.tuberlin.amos.ws17.swit.poi.PoiType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Checks the type parameter {@link PoiType} and {@link GoogleType} for the {@link GooglePoiService}
 */
public class TypeTest {

    private GooglePoiService loader;

    @Before
    public void constrution() throws ModuleNotWorkingException{
        try{
            loader=new GooglePoiService(TestData.apiKey, false, 100, 100, null);
        } catch (ModuleNotWorkingException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void simpleGoogleTypeTest() throws ModuleViolationException {

        List<GoogleType> typesToTest=new ArrayList<>();
        typesToTest.add(GoogleType.bank);
        typesToTest.add(GoogleType.bakery);
        typesToTest.add(GoogleType.zoo);

        //load pois for types
        List<GooglePoi> pois=loader.loadPlaceForCircleAndType(TestData.TIERGARTEN_POSITION_1,
                500, typesToTest.toArray(new GoogleType[typesToTest.size()]));

        assertTrue(pois.size()>1);
        assertTrue(pois.get(0).getPoiTypes().size()>0);
        assertTrue(pois.get(0).getTypes().size()>0);

        for(GoogleType type:typesToTest){
            boolean containsType=false;
            for(GooglePoi poi:pois){
                if(poi.getTypes().contains(type))
                    containsType=true;
            }
            assertTrue("The Type "+type.toString()+" could not be retrieved. That may result from the location or the radius."
                    , containsType);
        }

        System.out.println(pois.get(0).toString());
        System.out.println(pois.get(0).getTypes());
        System.out.println(pois.get(0).getPoiTypes());

    }

    @Test
    public void simplePoiTypeTest() throws ModuleViolationException {

        List<PoiType> typesToTest=new ArrayList<>();
        typesToTest.add(PoiType.TRANSPORT);
        typesToTest.add(PoiType.SHOPPING);
        typesToTest.add(PoiType.FOOD);

        //load pois for types
        List<GooglePoi> pois=loader.loadPlaceForCircleAndPoiType(TestData.TIERGARTEN_POSITION_1,
                500, typesToTest.toArray(new PoiType[typesToTest.size()]));

        assertTrue(pois.size()>1);
        assertTrue(pois.get(0).getPoiTypes().size()>0);
        assertTrue(pois.get(0).getTypes().size()>0);

        for(PoiType type:typesToTest){
            boolean containsType=false;
            for(GooglePoi poi:pois){
                if(poi.getPoiTypes().contains(type))
                    containsType=true;
            }
            assertTrue("The Type "+ type +" could not be retrieved. That may result from the location or the radius."
                    , containsType);
            System.out.println("Type PoiType" + type +" is being contained in the result.");
        }

        System.out.println(pois.get(0).toString());
        System.out.println(pois.get(0).getTypes());
        System.out.println(pois.get(0).getPoiTypes());

    }
}
