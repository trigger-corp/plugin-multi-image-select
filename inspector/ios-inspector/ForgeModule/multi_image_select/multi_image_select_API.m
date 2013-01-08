//
//	multi_image_select_API.m
//	ForgeInspector
//
//	Created by Antoine van Gelder on 27/11/2012.
//	Copyright (c) 2012 Trigger Corp. All rights reserved.
//

#import "multi_image_select_API.h"


/**
 * multi_image_select_API
 */
@implementation multi_image_select_API

+ (void) getImages:(ForgeTask*)task {
	multi_image_select_API *api = ((multi_image_select_API*)task.self);
    
    NSBundle *pickerBundle = [NSBundle bundleWithPath:[[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:@"multi_image_select.bundle"]];
    
	ELCAlbumPickerController *albumController = [[ELCAlbumPickerController alloc] initWithNibName:@"ELCAlbumPickerController" bundle:pickerBundle];
	ELCImagePickerController *pickerController = [[ELCImagePickerController alloc] initWithRootViewController:albumController];
	[albumController setParent:pickerController];

	api->pickerDelegate = [[ImagePickerDelegate alloc] initWithTask: task];
	[pickerController setDelegate:api->pickerDelegate];
	
	AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
	[app.viewController presentModalViewController:pickerController animated:YES];
}

@end



/**
 * ImagePickerDelegate
 */
@implementation ImagePickerDelegate

- (ImagePickerDelegate*) initWithTask:(ForgeTask *)forgetask {
	if (self = [super init]) {
		task = forgetask;
	}
	return self;
}

#pragma mark ELCImagePickerControllerDelegate Methods

- (void) elcImagePickerController:(ELCImagePickerController *)picker didFinishPickingMediaWithInfo:(NSArray *)info {
	[picker dismissModalViewControllerAnimated:YES];
	
	NSMutableArray *images = [NSMutableArray arrayWithCapacity:2];
	
	for (NSDictionary *dict in info) {
		NSMutableDictionary *file = [NSMutableDictionary dictionaryWithCapacity:2];
		[file setValue:[[dict objectForKey:UIImagePickerControllerReferenceURL] absoluteString] forKey:@"uri"];
		[file setValue:@"image" forKey:@"type"];
		[images addObject:file];
	}

	[task success:images];
}

- (void) elcImagePickerControllerDidCancel:(ELCImagePickerController *)picker {
	[picker dismissModalViewControllerAnimated:YES];
	[task success:[NSMutableArray arrayWithCapacity:0]];
}

@end



/**
 * AppDelegate
 */
@implementation AppDelegate
@synthesize viewController;
@end

