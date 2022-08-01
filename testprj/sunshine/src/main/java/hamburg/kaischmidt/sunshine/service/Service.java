package hamburg.kaischmidt.sunshine.service;

import hamburg.kaischmidt.sunshine.domain.Data;
import hamburg.kaischmidt.sunshine.domain.DataDomainService;
import lombok.Getter;

public class Service {

    @Getter
    private static Service service = new Service();

    public void processInput(String input) {
        DataDomainService.getDomainService().data(new Data(input));
    }

    public boolean hasData() {
        return DataDomainService.getDomainService().isThereDataToProcess();
    }

    public Data getData() {
        return DataDomainService.getDomainService().getData();
    }
}
