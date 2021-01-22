import { DataStream } from "../modules/DataStream";
import http from "../modules/http";
import { INote } from "./interfaces/INote";
import BaseStore from "./BaseStore";

class _NotesStore extends BaseStore {
  public notesStream = new DataStream<INote[] | undefined>(undefined);
  public sharedNotesStream = new DataStream<INote[] | undefined>(undefined);

  public requestNotes = async (): Promise<INote[]> => {
    try {
      this.notesStream.update({pending: 'loading'});

      const notesResponse = await this.makeRequest().get<INote[]>('notes');

      this.notesStream.update({
        pending: "done",
        data: notesResponse.data,
      })

      return notesResponse.data;
    } catch (e) {
      console.error(e);
      this.notesStream.update({pending: 'clear'})
      throw new Error(e);
    }
  }

  public requestSharedNotes = async (): Promise<INote[]> => {
    try {
      this.sharedNotesStream.update({pending: 'loading'});

      const notesResponse = await this.makeRequest().get<INote[]>('shared-notes');

      this.sharedNotesStream.update({
        pending: "done",
        data: notesResponse.data,
      })

      return notesResponse.data;
    } catch (e) {
      console.error(e);
      this.sharedNotesStream.update({pending: 'clear'})
      throw new Error(e);
    }
  }

  public createNote = async (note: {text: string}): Promise<INote> => {
    try {
      const response = await this.makeRequest().post<INote>('note', note);

      const prevNotes = this.notesStream.getValue().data;
      this.notesStream.update({
        pending: 'done',
        data: [...prevNotes, response.data],
      })

      return response.data;
    } catch (e) {
      console.error(e);
      throw new Error(e);
    }
  }

  public deleteNote = async (note: INote) => {
    try {
      const response = await this.makeRequest().delete(`note/${note.id}`);

      const prevNotes = this.notesStream.getValue().data;
      this.notesStream.update({
        pending: 'done',
        data: prevNotes ? prevNotes.filter(n => n.id !== note.id) : prevNotes
      });

      return response.data;
    } catch (e) {
      console.error(e);
      throw new Error(e);
    }
  }

  public editNote = async (note: INote): Promise<INote> => {
    try {
      const response = await this.makeRequest().put<INote>(`/note/${note.id}`, note);

      const prevNotes = this.notesStream.getValue().data;
      this.notesStream.update({
        pending: 'done',
        data: prevNotes ? prevNotes.map(n => n.id === note.id ? response.data : n) : prevNotes
      });

      return response.data;
    } catch (e) {
      console.error(e);
      throw new Error(e);
    }
  }

  public share = async (note_id: number, receiver_user_id: number) => {
    try {
      const response = await this.makeRequest().post(`/note-share`, {
        note_id, receiver_user_id,
      });

      return response.data;
    } catch (e) {
      console.error(e);
      throw new Error(e);
    }
  }
}

const NotesStore = new _NotesStore();

export default NotesStore;
