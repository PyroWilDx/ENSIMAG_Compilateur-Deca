initVariables:
    ld a, 0
	ld [wFrameCounter], a
	ld [wCurKeys], a
	ld [wNewKeys], a
	ret
CopyDEintoMemoryAtHL::
    ld a, [de]
    ld [hli], a
    inc de
    dec bc
    ld a, b
    or a, c
    jp nz, CopyDEintoMemoryAtHL ; Jump to COpyTiles, if the z flag is not set. (the last operation had a non zero result)
    ret;