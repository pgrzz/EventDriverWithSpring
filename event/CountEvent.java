package EventModel.event;

/**
 * Created by lenovo on 2016/10/9.
 */
public class CountEvent extends Event{

    //对于折扣事件的话
    private int zhekou;

    private String task;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getZhekou() {
        return zhekou;
    }

    public void setZhekou(int zhekou) {
        this.zhekou = zhekou;
    }
}
