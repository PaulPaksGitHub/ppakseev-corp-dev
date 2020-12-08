import { DataStream } from "../modules/DataStream";
import http from "../modules/http";
import { INote } from "./interfaces/INote";

class _NotesStore {
  public notesStream = new DataStream<INote[] | undefined>(undefined);

  public requestNotes = async (): Promise<INote[]> => {
    try {
      this.notesStream.update({pending: 'loading'});

      const notesResponse = await http.get<INote[]>('notes');

      this.notesStream.update({
        pending: "done",
        data: notesResponse.data,
      })

      return notesResponse.data;
    } catch (e) {
      console.error(e);
      this.notesStream.update({pending: 'clear'})
      return Promise.reject(e);
    }
  }

  public createNote = async (note: {text: string}): Promise<INote> => {
    try {
      const response = await http.post<INote>('note', note);

      const prevNotes = this.notesStream.getValue().data;
      this.notesStream.update({
        pending: 'done',
        data: [...prevNotes, response.data],
      })

      return response.data;
    } catch (e) {
      console.error(e);
      return Promise.reject(e);
    }
  }

  public deleteNote = async (note: INote) => {
    try {
      const response = await http.delete(`note/${note.id}`);

      const prevNotes = this.notesStream.getValue().data;
      this.notesStream.update({
        pending: 'done',
        data: prevNotes ? prevNotes.filter(n => n.id !== note.id) : prevNotes
      });

      return response.data;
    } catch (e) {
      console.error(e);
      return Promise.reject(e);
    }
  }

  public editNote = async (note: INote): Promise<INote> => {
    try {
      const response = await http.put<INote>(`/note/${note.id}`, note);

      const prevNotes = this.notesStream.getValue().data;
      this.notesStream.update({
        pending: 'done',
        data: prevNotes ? prevNotes.map(n => n.id === note.id ? response.data : n) : prevNotes
      });

      return response.data;
    } catch (e) {
      console.error(e);
      return Promise.reject(e);
    }
  }
}

const NotesStore = new _NotesStore();

export default NotesStore;
