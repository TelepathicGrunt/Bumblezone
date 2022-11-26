
import { fromFileUrl , dirname , join } from 'Path'


const folder = dirname(fromFileUrl(import.meta.url));

const root = join(folder,'..');


export const Data = 
    join(root,'Data');

export const Output =
    join(root,'..');
