package note.wic.FinalProject.activites.Listeners;



public interface OnListItemClickListeners {

    void onListItemDelted(String id, int adapterPos);
    void onListItemEdited(String id, int adapterPos, String item_name);
    void onItemClicked(String id, int adapterPos);
}
