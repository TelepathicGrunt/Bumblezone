
import * as Paths from 'Paths'
import findPages from './Pages.js'
import generate from './Markdown/Generate.js'


const { clear , log } = console;


clear();

log(`Building changelog from /Data/ folder.`);


for await ( const page of findPages() )
    await generate(page);
