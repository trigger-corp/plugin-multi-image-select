//
//	gallerypicker_API.m
//	ForgeInspector
//
//	Created by Antoine van Gelder on 27/11/2012.
//	Copyright (c) 2012 Trigger Corp. All rights reserved.
//

#import "gallerypicker_API.h"


/**
 * gallerypicker_API
 */
@implementation gallerypicker_API

+ (void) getImages:(ForgeTask*)task {
	gallerypicker_API *api = ((gallerypicker_API*)task.self);
	
	ELCAlbumPickerController *albumController = [[ELCAlbumPickerController alloc] initWithNibName:@"ELCAlbumPickerController" bundle:[NSBundle mainBundle]];
	ELCImagePickerController *pickerController = [[ELCImagePickerController alloc] initWithRootViewController:albumController];
	[albumController setParent:pickerController];

	api->pickerDelegate = [[ImagePickerDelegate alloc] initWithTask: task];
	[pickerController setDelegate:api->pickerDelegate];
	
	AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
	[app.viewController presentModalViewController:pickerController animated:YES];
}


+ (void) URL:(ForgeTask*)task file:(id)file {
	// TODO: Direct path to local files if possible?
	NSString *url = @"content://forge-content/?";
	for (NSString *key in file) {
		// Objective C doesn't do this very well - Encode strings to be put in URI.
		NSString *encodedKey = (__bridge_transfer NSString *)CFURLCreateStringByAddingPercentEscapes(NULL,
																									 (__bridge CFStringRef)key,
																									 NULL,
																									 (CFStringRef)@"!*'();:@&=+$,/?%#[]",
																									 kCFStringEncodingUTF8);
		NSString *encodedVal = (__bridge_transfer NSString *)CFURLCreateStringByAddingPercentEscapes(NULL,
																									 (__bridge CFStringRef)[[file objectForKey:key] description],
																									 NULL,
																									 (CFStringRef)@"!*'();:@&=+$,/?%#[]",
																									 kCFStringEncodingUTF8);
		url = [url stringByAppendingFormat:@"%@=%@&", encodedKey, encodedVal];
	}
	[task success:url];
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

