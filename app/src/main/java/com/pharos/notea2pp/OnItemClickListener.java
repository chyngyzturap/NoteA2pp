package com.pharos.notea2pp;

import com.pharos.notea2pp.model.Note;

public interface OnItemClickListener {
    void onClick(int position, Note note);
    void longClick(int position, Note note);
}
