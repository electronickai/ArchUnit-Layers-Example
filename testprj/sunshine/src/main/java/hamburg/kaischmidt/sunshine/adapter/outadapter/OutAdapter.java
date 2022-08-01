package hamburg.kaischmidt.sunshine.adapter.outadapter;

import hamburg.kaischmidt.sunshine.service.Service;

public class OutAdapter implements Runnable {
    public OutAdapter() {
    }

    public void run() {
        Service service = Service.getService();

        while(service.hasData()) {
            System.out.println("Out: " + service.getData());
        }

    }
}
