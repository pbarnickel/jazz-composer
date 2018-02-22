/*
    Description:    Chords for compositions.
                    //https://www.apassion4jazz.net/keys.html
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.Interfaces;

public interface Chords {
    int[] CH_MAJOR = new int[]{0,4,7};
    int[] CH_MINOR = new int[]{0,3,7};
    int[] CH_5 = new int[]{0,7};
    int[] CH_DOMINANT_7 = new int[]{0,4,7,10};
    int[] CH_MAJOR_7 = new int[]{0,4,7,11};
    int[] CH_MINOR_7 = new int[]{0,3,7,10};
    int[] CH_MINOR_MAJOR_7 = new int[]{0,3,7,11};
    int[] CH_SUS_4 = new int[]{0,5,7};
    int[] CH_SUS_2 = new int[]{0,2,7};
    int[] CH_6 = new int[]{0,4,7,9};
    int[] CH_MINOR_6 = new int[]{0,3,7,9};
    int[] CH_9 = new int[]{0,2,4,7,10};
    int[] CH_MINOR_9 = new int[]{0,2,3,7,10};
    int[] CH_MAJOR_9 = new int[]{0,2,4,7,11};
    int[] CH_MINOR_MAJOR_9 = new int[]{0,2,3,7,11};
    int[] CH_11 = new int[]{0,2,4,5,7,10};
    int[] CH_MINOR_11 = new int[]{0,2,3,5,7,10};
    int[] CH_MAJOR_11 = new int[]{0,2,4,5,7,11};
    int[] CH_MINOR_MAJOR_11 = new int[]{0,2,3,5,7,11};
    int[] CH_13 = new int[]{0,2,4,7,9,10};
    int[] CH_MINOR_13 = new int[]{0,2,3,7,9,10};
    int[] CH_MAJOR_13 = new int[]{0,2,4,7,9,11};
    int[] CH_MINOR_MAJOR_13 = new int[]{0,2,3,7,9,11};
    int[] CH_ADD_9 = new int[]{0,2,4,7};
    int[] CH_MINOR_ADD_9 = new int[]{0,2,3,7};
    int[] CH_6_ADD_9 = new int[]{0,2,4,7,9};
    int[] CH_MINOR_6_ADD_9 = new int[]{0,2,3,7,9};
    int[] CH_DOMINANT_7_ADD_11 = new int[]{0,4,5,7,10};
    int[] CH_MAJOR_7_ADD_11 = new int[]{0,4,5,7,11};
    int[] CH_MINOR_7_ADD_11 = new int[]{0,3,5,7,10};
    int[] CH_MINOR_MAJOR_7_ADD_11 = new int[]{0,3,5,7,11};
    int[] CH_DOMINANT_7_ADD_13 = new int[]{0,4,7,9,10};
    int[] CH_MAJOR_7_ADD_13 = new int[]{0,4,7,9,11};
    int[] CH_MINOR_7_ADD_13 = new int[]{0,3,7,9,10};
    int[] CH_MINOR_MAJOR_7_ADD_13 = new int[]{0,3,7,9,11};
    int[] CH_7B5 = new int[]{0,4,6,10};
    int[] CH_7S5 = new int[]{0,4,8,10};
    int[] CH_7B9 = new int[]{0,1,4,7,10};
    int[] CH_7S9 = new int[]{0,3,4,7,10};
    int[] CH_7S5B9 = new int[]{0,1,4,8,10};
    int[] CH_M7B5 = new int[]{0,3,6,10};
    int[] CH_M7S5 = new int[]{0,3,8,10};
    int[] CH_M7B9 = new int[]{0,1,3,7,10};
    int[] CH_9S11 = new int[]{0,2,4,6,7,10};
    int[] CH_9B13 = new int[]{0,2,4,7,8,10};
    int[] CH_6SUS4 = new int[]{0,5,7,9};
    int[] CH_7SUS4 = new int[]{0,5,7,10};
    int[] CH_MAJOR_7_SUS4 = new int[]{0,5,7,11};
    int[] CH_9SUS4 = new int[]{0,2,5,7,10};
    int[] CH_MAJOR_9_SUS4 = new int[]{0,2,5,7,11};
    int[][] CH_ARRAY = new int[][]{CH_MAJOR,CH_MINOR,CH_5,CH_DOMINANT_7,CH_MAJOR_7,CH_MINOR_7,CH_MINOR_MAJOR_7,CH_SUS_4,CH_SUS_2,CH_6,CH_MINOR_6,CH_9,CH_MINOR_9,CH_MAJOR_9,CH_MINOR_MAJOR_9,CH_11,CH_MINOR_11,CH_MAJOR_11,CH_MINOR_MAJOR_11,CH_13,CH_MINOR_13,CH_MAJOR_13,CH_MINOR_MAJOR_13,CH_ADD_9,CH_MINOR_ADD_9,CH_6_ADD_9,CH_MINOR_6_ADD_9,CH_DOMINANT_7_ADD_11,CH_MAJOR_7_ADD_11,CH_MINOR_7_ADD_11,CH_MINOR_MAJOR_7_ADD_11,CH_DOMINANT_7_ADD_13,CH_MAJOR_7_ADD_13,CH_MINOR_7_ADD_13,CH_MINOR_MAJOR_7_ADD_13,CH_7B5,CH_7S5,CH_7B9,CH_7S9,CH_7S5B9,CH_M7B5,CH_M7S5,CH_M7B9,CH_9S11,CH_9B13,CH_6SUS4,CH_7SUS4,CH_MAJOR_7_SUS4,CH_9SUS4,CH_MAJOR_9_SUS4};
}
