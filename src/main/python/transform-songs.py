import json
import os
from pathlib import Path
import collections

dataDir = '../webapp/static/data'

def songFile(performer, name):
  return dataDir + '/songs/' + performer + '/' + name + '.txt'

def writeFile(file, text):
  Path(os.path.dirname(file)).mkdir(parents=True, exist_ok=True)
  with open(file, "w+") as f:
    f.write(text)

with open(dataDir + '/songs.json') as f:
  songs = json.load(f)

songMap = collections.defaultdict(dict)
for song in songs:
  writeFile(songFile(song["performer"], song["name"]), song["text"])
  songMap[song["performer"]][song["name"]] = False
songMap = dict(songMap)
writeFile(dataDir + '/songs.json', json.dumps(songMap, indent=2, ensure_ascii=False))
