package co.com.arka.secretpoc.r2dbc;

import co.com.arka.secretpoc.model.order.Order;
import co.com.arka.secretpoc.model.order.gateways.OrderRepository;
import co.com.arka.secretpoc.r2dbc.entity.OrderData;
import co.com.arka.secretpoc.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Order/* change for domain model */,
        OrderData/* change for adapter model */,
        String,
        OrderReactiveRepository
> implements OrderRepository {
    public OrderReactiveRepositoryAdapter(OrderReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Order.class/* change for domain model */));
    }

}
