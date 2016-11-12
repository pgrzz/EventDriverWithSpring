package EventModel.bus;

/**
 * Created by lenovo on 2016/9/12.
 */
public class DispatcherConfig {

    private boolean orderBy;

    public boolean isOrderBy() {
        return orderBy;
    }

    public void setOrderBy(boolean orderBy) {
        this.orderBy = orderBy;
    }

    public DispatcherConfig(boolean orderBy) {
        this.orderBy = orderBy;
    }
}
