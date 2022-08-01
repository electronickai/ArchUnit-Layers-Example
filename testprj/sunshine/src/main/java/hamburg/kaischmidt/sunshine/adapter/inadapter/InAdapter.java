package hamburg.kaischmidt.sunshine.adapter.inadapter;

import hamburg.kaischmidt.sunshine.service.Service;

public class InAdapter {
    public InAdapter() {
    }

    public static void fakeInput(String input) {
        Service.getService().processInput(input);
    }
}