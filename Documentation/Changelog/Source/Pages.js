
import { filename } from './Misc/Path.js'
import { parse } from 'YAML'
import { walk } from 'FileSystem'
import { Data } from 'Paths'


const { readTextFile } = Deno;


const SearchOptions = {
    followSymlinks : false ,
    includeFiles : true ,
    includeDirs : false ,
    maxDepth : 1 ,
    exts : [ 'yaml' , 'yml' ]
}


export default async function * loadData (){

    const files = walk(Data,SearchOptions);

    for await ( const { path } of files ){
        
        const yaml = await readTextFile(path);
        
        const
            versions = parse(yaml) ,
            name = filename(path) ;

        yield { versions , name }
    }
}
