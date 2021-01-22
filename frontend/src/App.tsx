import * as React from 'react'
import AddNoteForm from './modules/AddNoteForm';
import NoteItem from './modules/NoteItem';
import { Container, Grid, Typography } from '@material-ui/core';
import { useDataStream } from './core/hooks/useBehaviorSubject';
import NotesStore from './core/stores/NotesStore';
import {useEffect} from "react";
import RegisterPage from "./pages/RegisterPage/RegisterPage";
import NotesPage from "./pages/NotesPage/NotesPage";
import {Route} from "./core/modules/Navigation";
import LoginPage from "./pages/LoginPage/LoginPage";

const App = () => {
  return (
      <div>
        <Route exact path={"/"}>
          <RegisterPage />
        </Route>
        <Route path={"/login"}>
          <LoginPage />
        </Route>
        <Route path={"/notes"}>
          <NotesPage />
        </Route>
      </div>
  )
}

export default App;
