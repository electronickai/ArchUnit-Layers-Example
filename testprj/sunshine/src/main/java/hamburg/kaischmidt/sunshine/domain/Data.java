package hamburg.kaischmidt.sunshine.domain;

import java.time.LocalDateTime;

@lombok.Data
public class Data {
    private String text = "";
    private LocalDateTime created = null;

    public Data(String inText) {
        this.text = inText;
    }

}