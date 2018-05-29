/*
    Description:    Class for handling Messages in the UI. At least the handover of the output label is required.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018

        Message-Types:
            0   -   Information
            1   -   Warning
            2   -   Error
            3   -   Success
 */

package composer.Model;

import javafx.scene.control.Label;

public class Response {

    private String message;
    private int message_type;

    public Response(String message, int message_type, Label label){
        this.message = message;
        this.message_type = message_type;
        print(label);
    }

    public void setMessage_type(int message_type){
        this.message_type = message_type;
    }

    //Clears the label used for UI-outputs
    public void clear(Label label){
        this.message = "";
        this.message_type = 0;
        print(label);
    }

    //Prints the message on the label used for UI-outputs
    private void print(Label label){
        label.getStyleClass().removeAll("msg_i", "msg_w", "msg_e", "msg_s");
        switch (this.message_type){
            case 0:     this.message = "ⓘ   " + this.message;
                        label.getStyleClass().add("msg_i");
                        break;
            case 1:     this.message = "❗   " + this.message;
                        label.getStyleClass().add("msg_w");
                        break;
            case 2:     this.message = "❌   " + this.message;
                        label.getStyleClass().add("msg_e");
                        break;
            case 3:     this.message = "✔   " + this.message;
                        label.getStyleClass().add("msg_s");
                        break;
            default:    this.message = "\uD83D\uDEC8    " + this.message;
                        this.setMessage_type(0);
                        print(label);
        }
        label.setText(this.message);
    }

}
