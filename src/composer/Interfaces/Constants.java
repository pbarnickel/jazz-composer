/*
    Description:    Interface for Constants
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           18.05.2018
 */

package composer.Interfaces;

import composer.DataClasses.Range;

public interface Constants {

    //############ MESSAGE-TYPES ######################
    int MSG_I = 0;                  //Information
    int MSG_W = 1;                  //Warning
    int MSG_E = 2;                  //Error
    int MSG_S = 3;                  //Success

    //############ REGULAR EXPRESSIONS ################
    String REG_CHORD_NAME = "[a-zA-Z0-9#]+[a-zA-Z0-9_# ]*";
    String REG_CHORD_USAGE = "[0-9]+(-[0-9]+)*";
    String REG_TONE = "[cdefgabCDEFGAB]{1}";
    String REG_TONE_EXTENDED = "[abcdefgabhCDEFGABH]{1}[#b]?";
    String REG_TEMPO = "-?\\d+";
    String REG_NUMBER = "[1-9][0-9]*";
    String REG_NUMBER_0 = "[0-9]+";
    String REG_NAME = "[a-zA-Z]+[a-z-A-Z0-9 ]*";
    String REG_TRANSPOSE = "[-]*[0-9]+";
    String REG_TACT_PROPORTION = "Full|Semi";
    String REG_PITCH = "[0-9]+([.][0-9]+)?";

    //############ OTHER VALUES ######################
    int OV_HUMANIZER_PERCENTAGE = 3;
    int OV_MAX_NR_OF_BAR_USES_PIANO = 3;
    int OV_MAX_NR_OF_BAR_USES_TRUMPET = 4;
    Range OV_TRUMPET_BEBOP_NR_OF_USES_IN_BAR = new Range(6,8);
    Range OV_TRUMPET_NORMAL_NR_OF_USES_IN_BAR = new Range(0,6);
}
