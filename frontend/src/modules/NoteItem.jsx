import React, {useState} from 'react';
import { Grid, Typography, Card, CardContent, Box, Button, TextField } from '@material-ui/core';
import http from '../core/http';

const NoteItem = props => {
  const [isEditing, setIsEditing] = useState(false);
  const [displayedText, setDisplayedText] = useState(props.text || "");

  const onPressDeleteNote = () => {
    const wannaDelete = confirm("Вы уверены, что хотите удалить заметку?");
    if (wannaDelete) {
      http.delete(`note-delete?id=${props.id}`)
        .then(() => {
          props.onNoteDeleted && props.onNoteDeleted();
        })
    }
  }

  const onPressEditNote = () => {
    setIsEditing(true);
  }

  const onPressSubmitEditing = () => {
    setIsEditing(false);
    http.put('note-edit', {
      id: props.id,
      text: displayedText,
    }).then(response => {
      props.onNodeEdited && props.onNodeEdited(response.data);
    })
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