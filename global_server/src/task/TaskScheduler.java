package task;

import config.Messages;

import java.util.ArrayList;

public class TaskScheduler {

    private ArrayList<Task> queue;
    private int count;

    /**
     * Constructor
     */
    public TaskScheduler()
    {
        this.count = 0;
        this.queue = new ArrayList<Task>();
    }

    /**
     * Adding a task
     * @param task
     */
    public boolean add(Task task)
    {
        if(!task.status.equals(Messages.task_completed))
        {
            if(task.analyseParameters())
            {
                task.task_id = count;
                queue.add(task);
                count++;
            }else
            {
                System.err.println("Error: Cannot Add to Scheduler : Problem with Task");
                return false;
            }
        }else
        {
            System.err.println("Error: Cannot Add to Scheduler : Task Completed");
            return false;
        }
        return true;
    }

    /**
     * Removing a task
     * @param task
     */
    public void remove(Task task)
    {
        for(int i=0;i<queue.size();i++)
        {
            Task t = queue.get(i);
            if(t.task_id == task.task_id)
            {
                if(t.status.equals(Messages.task_completed))
                {
                    queue.remove(task);
                    break;
                } else
                {
                    System.err.println("Error: Cannot Remove from Scheduler : Task Not Completed");
                }
            }
        }
    }

    /**
     * Updating task status
     * @param task Task
     * @param status String
     */
    public void updateTaskStatus(Task task, String status)
    {
        for(int i=0;i<queue.size();i++) {
            Task t = queue.get(i);
            if (t.task_id == task.task_id) {
                t.status = status;
            }
        }
    }

    /**
     * Updating task status
     * @param task_id Integer
     * @param rawStatus String
     */
    public void updateTaskStatus(int task_id, String rawStatus)
    {
        for(int i=0;i<queue.size();i++) {
            Task t = queue.get(i);
            if (t.task_id == task_id) {
                if(rawStatus.equals(Messages.response_OK))
                {
                    //TODO remove completed task
                    t.status = Messages.task_completed;
                }
            }
        }
    }

    @Override
    public String toString()
    {
        return "TaskScheduler{" +
                "queue=" + queue +
                ", count=" + count +
                '}';
    }

    /**
     * Test
     * Main Method
     * @param args
     */
    public static void main(String[] args)
    {
        TaskScheduler scheduler = new TaskScheduler();
        Task task1 = new Task("barcelona",0,0);
        Task task2 = new Task("sunny",0,0);
        Task task3 = new Task("sunny",0,0);
        Task task4 = new Task("barcelona",0,0);

        scheduler.add(task1);
        scheduler.add(task2);
        scheduler.add(task3);
        System.out.println(scheduler);
        scheduler.updateTaskStatus(task2, Messages.task_completed);
        scheduler.remove(task2);
        scheduler.add(task4);
        System.out.println(scheduler);
    }

}
