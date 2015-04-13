package depreciated_master;
import java.util.LinkedList;



public class TasksBoard {
	private LinkedList <Task> tasks = new LinkedList <Task> ();
	
	int lastID = 0;
	int newID = lastID + 1;
	
	void addNewTask(Task task){
		task.id = newID;
		newID++;
		tasks.add(task);
	}
	
	
	public Task getTask(int id){
		
		for (Task t : tasks){
			if (t.id == id){
				return t;
			}
		}
		
		return null;
	}
	
	
	
	
	public LinkedList<Task> getTasks() {
		return tasks;
	}
	
	
	public int getTasksCount(){
		return getTasks().size();
	}
	
// get tasks by status
	public LinkedList<Task> getOpenTasks() {
		LinkedList <Task> openTasks = new LinkedList <Task> ();
		for (Task t : tasks){
			if(t.status.equals("open")){
				openTasks.add(t);
			}
		}
		return openTasks;
	}


	public LinkedList<Task> getTasksInProgress() {
		LinkedList <Task> tasksInProgress = new LinkedList <Task> ();
		for (Task t : tasks){
			if(t.status.equals("inProgress")){
				tasksInProgress.add(t);
			}
		}
		return tasksInProgress;
	}


	public LinkedList<Task> getClosedTasks() {
		LinkedList <Task> closedTasks = new LinkedList <Task> ();
		for (Task t : tasks){
			if(t.status.equals("closed")){
				closedTasks.add(t);
			}
		}
		return closedTasks;
	}

	


// get open tasks by type
	
	public LinkedList<Task> getOpenMapSelectTasks() {
		LinkedList <Task> result = new LinkedList <Task> ();
		
		for (Task task : tasks)	{ 
			if (task.status.equals("open") && task.type.equals("MapSelect")){
				result.add(task);
			}
		}
		return result;
	}
	
	
	public LinkedList<Task> getOpenMapCountTasks() {
		LinkedList <Task> result = new LinkedList <Task> ();
		
		for (Task task : tasks)	{ 
			if (task.status.equals("open") && task.type.equals("MapCount")){
				result.add(task);
			}
		}
		return result;
	}
	
	
	public LinkedList<Task> getOpenMapTasks() {
		LinkedList <Task> result = new LinkedList <Task> ();
		
		for (Task task : tasks)	{ 
			if (task.status.equals("open") && (task.type.equals("MapCount") || task.type.equals("MapSelect"))  ){
				result.add(task);
			}
		}
		return result;
	}
	
	
	public LinkedList<Task> getOpenReduceTasks() {
		LinkedList <Task> result = new LinkedList <Task> ();
		
		for (Task task : tasks)	{ 
			if (task.status.equals("open") && task.type.equals("ReduceMerge") ){
				result.add(task);
			}
		}
		return result;
	}
	
	
	// New methods //TODO
	/*
	public LinkedList<Task> getOpenTasksByStatus(String status) {
		LinkedList <Task> result = new LinkedList <Task>();
		
		for (Task task : tasks)	{
			if (task.status.equals(status))		{ result.add(task); }
		}
		
		return result;
	}
	
	
	public LinkedList<Task> getTasksByType(String type) {
		LinkedList <Task> result = new LinkedList <Task>();
		
		for (Task task : tasks)	{
				if (task.type.equals(type))		{ result.add(task); }
		}
		
		return result;
	}
	
	*/

}
