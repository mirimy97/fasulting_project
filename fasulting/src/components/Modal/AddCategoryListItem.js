import React from "react"
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Collapse from '@mui/material/Collapse';
import SubdirectoryArrowRightIcon from '@mui/icons-material/SubdirectoryArrowRight';
import Checkbox from '@mui/material/Checkbox';

function AddCategoryListItem ({mainId, mainName, subList, selected, increaseCnt, decreaseCnt}) {
  const selectedsub = selected.length === 0 ? [] : 
    selected[0].sub_ctg.map((subitem) => {
      // 카테고리 id는 1부터 시작인데, 인덱스는 0부터 시작이므로 -1
      return subitem.id-1
    })
  
  // 체크박스 state 생성 + 토글 함수 생성
  const [checked, setChecked] = React.useState(selectedsub);
  const handleToggle = (value) => () => {
  const currentIndex = checked.indexOf(value);
  const newChecked = [...checked];
  

  if (currentIndex === -1) {
    increaseCnt()
    newChecked.push(value);
  } else {
    decreaseCnt()
    newChecked.splice(currentIndex, 1);
  }

  setChecked(newChecked);
  };

  const currentopen = selectedsub.length === 0 ? false : true
  const [open, setOpen] = React.useState(currentopen);
  const handleClick = () => {
    setOpen((current) => !current)
  };

  return (
    <div>
      <ListItemButton 
      onClick={handleClick}
      sx={ open ? {background: '#E5F3F5'} : null}
      >
        <ListItemText primary={mainName} />
      </ListItemButton>
      {subList.map((sub, index) => {
        const labelId = `${mainId}-${sub.id}`
        return (
          <Collapse in={open} timeout="auto" unmountOnExit
          key={labelId}
          >
            <List component="div" disablePadding>
              <ListItemButton sx={{ pl: 4 }} onClick={handleToggle(index)} dense>
                <ListItemIcon>
                  <SubdirectoryArrowRightIcon />
                </ListItemIcon>
                <ListItemText id={labelId} primary={sub.name} />
                <ListItemIcon>
                  <Checkbox
                    edge="end"
                    checked={checked.indexOf(index) !== -1}
                    tabIndex={-1}
                    disableRipple
                    inputProps={{ 'aria-labelledby': labelId }}
                  />
                </ListItemIcon>
              </ListItemButton>
            </List>
          </Collapse>
        )
      })}
      </div>
  )
}

export default AddCategoryListItem