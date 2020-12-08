import * as React from 'react';
import {useState} from 'react';
import {Box, Button, Card, CardContent, Grid, TextField, Typography} from '@material-ui/core';
import NotesStore from "../core/stores/NotesStore";
import {INote} from "../core/stores/interfaces/INote";

interface IProps extends INote {

}

const NoteItem: React.FunctionComponent<IProps> = props => {
  const [isEditing, setIsEditing] = useState(false);
  const [displayedText, setDisplayedText] = useState(props.text || "");

  const onPressDeleteNote = () => {
    const wannaDelete = confirm("Вы уверены, что хотите удалить заметку?");
    if (wannaDelete) {
      NotesStore.deleteNote(props);
    }
  }

  const onPressEditNote = () => {
    setIsEditing(true);
  }

  const onPressSubmitEditing = () => {
    setIsEditing(false);
    NotesStore.editNote({
      id: props.id,
      text: displayedText,
    });
  }

  const onPressCloseEditing = () => {
    setIsEditing(false);
    setDisplayedText(props.text);
  }

  return (
    <Grid item xs={6}>
      <Card>
        <CardContent>
          {isEditing ? (
            <>
              <TextField multiline id="standard-basic" label="Редактировать" value={displayedText} onChange={e => {
                setDisplayedText(e.target.value)
              }} />
              <Box py={1} flexDirection="row">
                <Button onClick={onPressSubmitEditing} color="primary" disabled={displayedText.trim().length === 0}>Сохранить</Button>
                <Button onClick={onPressCloseEditing} color="secondary">Отменить</Button>
              </Box>
            </>
          ) : (
              <>
                <Typography>{displayedText}</Typography>
                <Box py={1} flexDirection="row">
                  <Button onClick={onPressEditNote} color="primary">Редактировать</Button>
                  <Button onClick={onPressDeleteNote} color="secondary">Удалить</Button>
                </Box>
              </>
            )}
        </CardContent>
      </Card>
    </Grid>
  )
}

export default NoteItem;
